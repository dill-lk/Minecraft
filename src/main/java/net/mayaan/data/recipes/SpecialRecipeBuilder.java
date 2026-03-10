/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.data.recipes;

import java.util.function.Supplier;
import net.mayaan.advancements.AdvancementHolder;
import net.mayaan.advancements.Criterion;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.recipes.RecipeCategory;
import net.mayaan.data.recipes.RecipeOutput;
import net.mayaan.data.recipes.RecipeUnlockAdvancementBuilder;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

public class SpecialRecipeBuilder {
    private @Nullable RecipeUnlockAdvancementBuilder advancementBuilder;
    private final Supplier<Recipe<?>> factory;

    public SpecialRecipeBuilder(Supplier<Recipe<?>> factory) {
        this.factory = factory;
    }

    public static SpecialRecipeBuilder special(Supplier<Recipe<?>> factory) {
        return new SpecialRecipeBuilder(factory);
    }

    public SpecialRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        if (this.advancementBuilder == null) {
            this.advancementBuilder = new RecipeUnlockAdvancementBuilder();
        }
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    public void save(RecipeOutput output, String name) {
        this.save(output, ResourceKey.create(Registries.RECIPE, Identifier.parse(name)));
    }

    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        AdvancementHolder advancement = this.advancementBuilder != null ? this.advancementBuilder.build(output, id, RecipeCategory.MISC) : null;
        output.accept(id, this.factory.get(), advancement);
    }
}

