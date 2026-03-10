/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.data.recipes;

import net.mayaan.advancements.Criterion;
import net.mayaan.advancements.criterion.MinMaxBounds;
import net.mayaan.data.recipes.RecipeBuilder;
import net.mayaan.data.recipes.RecipeCategory;
import net.mayaan.data.recipes.RecipeOutput;
import net.mayaan.data.recipes.RecipeUnlockAdvancementBuilder;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStackTemplate;
import net.mayaan.world.item.crafting.Ingredient;
import net.mayaan.world.item.crafting.Recipe;
import net.mayaan.world.item.crafting.TransmuteRecipe;
import org.jspecify.annotations.Nullable;

public class TransmuteRecipeBuilder
implements RecipeBuilder {
    private final RecipeCategory category;
    private final ItemStackTemplate result;
    private final Ingredient input;
    private final Ingredient material;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    private @Nullable String group;
    private MinMaxBounds.Ints materialCount = TransmuteRecipe.DEFAULT_MATERIAL_COUNT;
    private boolean addMaterialCountToOutput;

    private TransmuteRecipeBuilder(RecipeCategory category, ItemStackTemplate result, Ingredient input, Ingredient material) {
        this.category = category;
        this.result = result;
        this.input = input;
        this.material = material;
    }

    public static TransmuteRecipeBuilder transmute(RecipeCategory category, Ingredient input, Ingredient material, Item result) {
        return TransmuteRecipeBuilder.transmute(category, input, material, new ItemStackTemplate(result));
    }

    public static TransmuteRecipeBuilder transmute(RecipeCategory category, Ingredient input, Ingredient material, ItemStackTemplate result) {
        return new TransmuteRecipeBuilder(category, result, input, material);
    }

    @Override
    public TransmuteRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    @Override
    public TransmuteRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public TransmuteRecipeBuilder addMaterialCountToOutput() {
        this.addMaterialCountToOutput = true;
        return this;
    }

    public TransmuteRecipeBuilder setMaterialCount(MinMaxBounds.Ints materialCount) {
        this.materialCount = materialCount;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(this.result);
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        TransmuteRecipe recipe = new TransmuteRecipe(RecipeBuilder.createCraftingCommonInfo(true), RecipeBuilder.createCraftingBookInfo(this.category, this.group), this.input, this.material, this.materialCount, this.result, this.addMaterialCountToOutput);
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
    }
}

