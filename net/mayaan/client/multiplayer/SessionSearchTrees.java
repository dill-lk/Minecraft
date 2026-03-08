/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.multiplayer;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.mayaan.ChatFormatting;
import net.mayaan.client.ClientRecipeBook;
import net.mayaan.client.gui.screens.recipebook.RecipeCollection;
import net.mayaan.client.searchtree.FullTextSearchTree;
import net.mayaan.client.searchtree.IdSearchTree;
import net.mayaan.client.searchtree.SearchTree;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.Registry;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.ResourceKey;
import net.mayaan.tags.TagKey;
import net.mayaan.util.Util;
import net.mayaan.util.context.ContextMap;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.crafting.display.SlotDisplayContext;
import net.mayaan.world.level.Level;

public class SessionSearchTrees {
    private static final Key RECIPE_COLLECTIONS = new Key();
    private static final Key CREATIVE_NAMES = new Key();
    private static final Key CREATIVE_TAGS = new Key();
    private CompletableFuture<SearchTree<ItemStack>> creativeByNameSearch = CompletableFuture.completedFuture(SearchTree.empty());
    private CompletableFuture<SearchTree<ItemStack>> creativeByTagSearch = CompletableFuture.completedFuture(SearchTree.empty());
    private CompletableFuture<SearchTree<RecipeCollection>> recipeSearch = CompletableFuture.completedFuture(SearchTree.empty());
    private final Map<Key, Runnable> reloaders = new IdentityHashMap<Key, Runnable>();

    private void register(Key location, Runnable updater) {
        updater.run();
        this.reloaders.put(location, updater);
    }

    public void rebuildAfterLanguageChange() {
        for (Runnable value : this.reloaders.values()) {
            value.run();
        }
    }

    private static Stream<String> getTooltipLines(Stream<ItemStack> items, Item.TooltipContext context, TooltipFlag flag) {
        return items.flatMap(item -> item.getTooltipLines(context, null, flag).stream()).map(l -> ChatFormatting.stripFormatting(l.getString()).trim()).filter(s -> !s.isEmpty());
    }

    public void updateRecipes(ClientRecipeBook recipeBook, Level level) {
        this.register(RECIPE_COLLECTIONS, () -> {
            List<RecipeCollection> recipes = recipeBook.getCollections();
            RegistryAccess registryAccess = level.registryAccess();
            HolderLookup.RegistryLookup itemRegistries = registryAccess.lookupOrThrow(Registries.ITEM);
            Item.TooltipContext tooltipContext = Item.TooltipContext.of(registryAccess);
            ContextMap recipeContext = SlotDisplayContext.fromLevel(level);
            TooltipFlag.Default tooltipFlag = TooltipFlag.Default.NORMAL;
            CompletableFuture<SearchTree<RecipeCollection>> previous = this.recipeSearch;
            this.recipeSearch = CompletableFuture.supplyAsync(() -> SessionSearchTrees.lambda$updateRecipes$1(recipeContext, tooltipContext, tooltipFlag, (Registry)itemRegistries, recipes), Util.backgroundExecutor());
            previous.cancel(true);
        });
    }

    public SearchTree<RecipeCollection> recipes() {
        return this.recipeSearch.join();
    }

    public void updateCreativeTags(List<ItemStack> items) {
        this.register(CREATIVE_TAGS, () -> {
            CompletableFuture<SearchTree<ItemStack>> previous = this.creativeByTagSearch;
            this.creativeByTagSearch = CompletableFuture.supplyAsync(() -> new IdSearchTree<ItemStack>(itemStack -> itemStack.tags().map(TagKey::location), items), Util.backgroundExecutor());
            previous.cancel(true);
        });
    }

    public SearchTree<ItemStack> creativeTagSearch() {
        return this.creativeByTagSearch.join();
    }

    public void updateCreativeTooltips(HolderLookup.Provider registries, List<ItemStack> itemStacks) {
        this.register(CREATIVE_NAMES, () -> {
            Item.TooltipContext tooltipContext = Item.TooltipContext.of(registries);
            TooltipFlag.Default tooltipFlag = TooltipFlag.Default.NORMAL.asCreative();
            CompletableFuture<SearchTree<ItemStack>> previous = this.creativeByNameSearch;
            this.creativeByNameSearch = CompletableFuture.supplyAsync(() -> new FullTextSearchTree<ItemStack>(itemStack -> SessionSearchTrees.getTooltipLines(Stream.of(itemStack), tooltipContext, tooltipFlag), itemStack -> itemStack.typeHolder().unwrapKey().map(ResourceKey::identifier).stream(), itemStacks), Util.backgroundExecutor());
            previous.cancel(true);
        });
    }

    public SearchTree<ItemStack> creativeNameSearch() {
        return this.creativeByNameSearch.join();
    }

    private static /* synthetic */ SearchTree lambda$updateRecipes$1(ContextMap recipeContext, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, Registry itemRegistries, List recipes) {
        return new FullTextSearchTree<RecipeCollection>(collection -> SessionSearchTrees.getTooltipLines(collection.getRecipes().stream().flatMap(e -> e.resultItems(recipeContext).stream()), tooltipContext, tooltipFlag), collection -> collection.getRecipes().stream().flatMap(e -> e.resultItems(recipeContext).stream()).map(stack -> itemRegistries.getKey(stack.getItem())), recipes);
    }

    private static class Key {
        private Key() {
        }
    }
}

