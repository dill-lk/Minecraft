/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.recipes;

import java.util.Optional;
import net.mayaan.advancements.Criterion;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.recipes.RecipeCategory;
import net.mayaan.data.recipes.RecipeOutput;
import net.mayaan.data.recipes.RecipeUnlockAdvancementBuilder;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStackTemplate;
import net.mayaan.world.item.crafting.Ingredient;
import net.mayaan.world.item.crafting.Recipe;
import net.mayaan.world.item.crafting.SmithingTransformRecipe;

public class SmithingTransformRecipeBuilder {
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final RecipeCategory category;
    private final ItemStackTemplate result;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();

    public SmithingTransformRecipeBuilder(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, ItemStackTemplate result) {
        this.category = category;
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public static SmithingTransformRecipeBuilder smithing(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, Item result) {
        return new SmithingTransformRecipeBuilder(template, base, addition, category, new ItemStackTemplate(result));
    }

    public SmithingTransformRecipeBuilder unlocks(String name, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    public void save(RecipeOutput output, String id) {
        this.save(output, ResourceKey.create(Registries.RECIPE, Identifier.parse(id)));
    }

    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        SmithingTransformRecipe recipe = new SmithingTransformRecipe(new Recipe.CommonInfo(true), Optional.of(this.template), this.base, Optional.of(this.addition), this.result);
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
    }
}

