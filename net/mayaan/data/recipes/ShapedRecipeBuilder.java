/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.data.recipes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
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
import net.mayaan.world.item.crafting.ShapedRecipe;
import net.mayaan.world.item.crafting.ShapedRecipePattern;
import net.mayaan.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

public class ShapedRecipeBuilder
implements RecipeBuilder {
    private final HolderGetter<Item> items;
    private final RecipeCategory category;
    private final ItemStackTemplate result;
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    private @Nullable String group;
    private boolean showNotification = true;

    private ShapedRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemStackTemplate result) {
        this.items = items;
        this.category = category;
        this.result = result;
    }

    private ShapedRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemLike result, int count) {
        this(items, category, new ItemStackTemplate(result.asItem(), count));
    }

    public static ShapedRecipeBuilder shaped(HolderGetter<Item> items, RecipeCategory category, ItemLike item) {
        return ShapedRecipeBuilder.shaped(items, category, item, 1);
    }

    public static ShapedRecipeBuilder shaped(HolderGetter<Item> items, RecipeCategory category, ItemLike item, int count) {
        return new ShapedRecipeBuilder(items, category, item, count);
    }

    public ShapedRecipeBuilder define(Character symbol, TagKey<Item> tag) {
        return this.define(symbol, Ingredient.of(this.items.getOrThrow(tag)));
    }

    public ShapedRecipeBuilder define(Character symbol, ItemLike item) {
        return this.define(symbol, Ingredient.of(item));
    }

    public ShapedRecipeBuilder define(Character symbol, Ingredient ingredient) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        }
        if (symbol.charValue() == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        }
        this.key.put(symbol, ingredient);
        return this;
    }

    public ShapedRecipeBuilder pattern(String row) {
        if (!this.rows.isEmpty() && row.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        }
        this.rows.add(row);
        return this;
    }

    @Override
    public ShapedRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    @Override
    public ShapedRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public ShapedRecipeBuilder showNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(this.result);
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        ShapedRecipePattern pattern = ShapedRecipePattern.of(this.key, this.rows);
        ShapedRecipe recipe = new ShapedRecipe(RecipeBuilder.createCraftingCommonInfo(this.showNotification), RecipeBuilder.createCraftingBookInfo(this.category, this.group), pattern, this.result);
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
    }
}

