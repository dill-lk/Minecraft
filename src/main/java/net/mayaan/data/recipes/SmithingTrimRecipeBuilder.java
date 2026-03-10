/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.recipes;

import net.mayaan.advancements.Criterion;
import net.mayaan.core.Holder;
import net.mayaan.data.recipes.RecipeCategory;
import net.mayaan.data.recipes.RecipeOutput;
import net.mayaan.data.recipes.RecipeUnlockAdvancementBuilder;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.crafting.Ingredient;
import net.mayaan.world.item.crafting.Recipe;
import net.mayaan.world.item.crafting.SmithingTrimRecipe;
import net.mayaan.world.item.equipment.trim.TrimPattern;

public class SmithingTrimRecipeBuilder {
    private final RecipeCategory category;
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final Holder<TrimPattern> pattern;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();

    public SmithingTrimRecipeBuilder(RecipeCategory category, Ingredient template, Ingredient base, Ingredient addition, Holder<TrimPattern> pattern) {
        this.category = category;
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.pattern = pattern;
    }

    public static SmithingTrimRecipeBuilder smithingTrim(Ingredient template, Ingredient base, Ingredient addition, Holder<TrimPattern> pattern, RecipeCategory category) {
        return new SmithingTrimRecipeBuilder(category, template, base, addition, pattern);
    }

    public SmithingTrimRecipeBuilder unlocks(String name, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        SmithingTrimRecipe recipe = new SmithingTrimRecipe(new Recipe.CommonInfo(true), this.template, this.base, this.addition, this.pattern);
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
    }
}

