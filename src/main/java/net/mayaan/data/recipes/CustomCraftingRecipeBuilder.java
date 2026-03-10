/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.data.recipes;

import java.util.function.BiFunction;
import net.mayaan.advancements.Criterion;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.recipes.RecipeBuilder;
import net.mayaan.data.recipes.RecipeCategory;
import net.mayaan.data.recipes.RecipeOutput;
import net.mayaan.data.recipes.RecipeUnlockAdvancementBuilder;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.crafting.CraftingRecipe;
import net.mayaan.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

public class CustomCraftingRecipeBuilder {
    private final RecipeCategory category;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    private @Nullable String group;
    private final Factory factory;

    public CustomCraftingRecipeBuilder(RecipeCategory category, Factory factory) {
        this.category = category;
        this.factory = factory;
    }

    public static CustomCraftingRecipeBuilder customCrafting(RecipeCategory category, Factory factory) {
        return new CustomCraftingRecipeBuilder(category, factory);
    }

    public CustomCraftingRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    public CustomCraftingRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public void save(RecipeOutput output, String name) {
        this.save(output, ResourceKey.create(Registries.RECIPE, Identifier.parse(name)));
    }

    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        Recipe.CommonInfo commonInfo = RecipeBuilder.createCraftingCommonInfo(true);
        CraftingRecipe.CraftingBookInfo bookInfo = RecipeBuilder.createCraftingBookInfo(this.category, this.group);
        Recipe recipe = (Recipe)this.factory.apply(commonInfo, bookInfo);
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
    }

    @FunctionalInterface
    public static interface Factory
    extends BiFunction<Recipe.CommonInfo, CraftingRecipe.CraftingBookInfo, Recipe<?>> {
    }
}

