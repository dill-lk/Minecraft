/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.recipes;

import java.util.LinkedHashMap;
import java.util.Map;
import net.mayaan.advancements.Advancement;
import net.mayaan.advancements.AdvancementHolder;
import net.mayaan.advancements.AdvancementRequirements;
import net.mayaan.advancements.AdvancementRewards;
import net.mayaan.advancements.Criterion;
import net.mayaan.advancements.criterion.RecipeUnlockedTrigger;
import net.mayaan.data.recipes.RecipeCategory;
import net.mayaan.data.recipes.RecipeOutput;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.crafting.Recipe;

public class RecipeUnlockAdvancementBuilder {
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap();

    public void unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
    }

    public AdvancementHolder build(RecipeOutput output, ResourceKey<Recipe<?>> id, RecipeCategory category) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(id.identifier()));
        }
        Advancement.Builder advancement = output.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);
        return advancement.build(id.identifier().withPrefix("recipes/" + category.getFolderName() + "/"));
    }
}

