/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.data.recipes;

import java.util.ArrayList;
import java.util.List;
import net.mayaan.advancements.Criterion;
import net.mayaan.core.HolderGetter;
import net.mayaan.data.recipes.RecipeBuilder;
import net.mayaan.data.recipes.RecipeCategory;
import net.mayaan.data.recipes.RecipeOutput;
import net.mayaan.data.recipes.RecipeUnlockAdvancementBuilder;
import net.mayaan.resources.ResourceKey;
import net.mayaan.tags.TagKey;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStackTemplate;
import net.mayaan.world.item.crafting.Ingredient;
import net.mayaan.world.item.crafting.Recipe;
import net.mayaan.world.item.crafting.ShapelessRecipe;
import net.mayaan.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

public class ShapelessRecipeBuilder
implements RecipeBuilder {
    private final HolderGetter<Item> items;
    private final RecipeCategory category;
    private final ItemStackTemplate result;
    private final List<Ingredient> ingredients = new ArrayList<Ingredient>();
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    private @Nullable String group;

    private ShapelessRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemStackTemplate result) {
        this.items = items;
        this.category = category;
        this.result = result;
    }

    public static ShapelessRecipeBuilder shapeless(HolderGetter<Item> items, RecipeCategory category, ItemStackTemplate result) {
        return new ShapelessRecipeBuilder(items, category, result);
    }

    public static ShapelessRecipeBuilder shapeless(HolderGetter<Item> items, RecipeCategory category, ItemLike item) {
        return ShapelessRecipeBuilder.shapeless(items, category, item, 1);
    }

    public static ShapelessRecipeBuilder shapeless(HolderGetter<Item> items, RecipeCategory category, ItemLike item, int count) {
        return new ShapelessRecipeBuilder(items, category, new ItemStackTemplate(item.asItem(), count));
    }

    public ShapelessRecipeBuilder requires(TagKey<Item> tag) {
        return this.requires(Ingredient.of(this.items.getOrThrow(tag)));
    }

    public ShapelessRecipeBuilder requires(ItemLike item) {
        return this.requires(item, 1);
    }

    public ShapelessRecipeBuilder requires(ItemLike item, int count) {
        for (int i = 0; i < count; ++i) {
            this.requires(Ingredient.of(item));
        }
        return this;
    }

    public ShapelessRecipeBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    public ShapelessRecipeBuilder requires(Ingredient ingredient, int count) {
        for (int i = 0; i < count; ++i) {
            this.ingredients.add(ingredient);
        }
        return this;
    }

    @Override
    public ShapelessRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    @Override
    public ShapelessRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(this.result);
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        ShapelessRecipe recipe = new ShapelessRecipe(RecipeBuilder.createCraftingCommonInfo(true), RecipeBuilder.createCraftingBookInfo(this.category, this.group), this.result, this.ingredients);
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
    }
}

