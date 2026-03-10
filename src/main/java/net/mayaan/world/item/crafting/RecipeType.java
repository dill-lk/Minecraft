/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.crafting;

import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.resources.Identifier;
import net.mayaan.world.item.crafting.BlastingRecipe;
import net.mayaan.world.item.crafting.CampfireCookingRecipe;
import net.mayaan.world.item.crafting.CraftingRecipe;
import net.mayaan.world.item.crafting.Recipe;
import net.mayaan.world.item.crafting.SmeltingRecipe;
import net.mayaan.world.item.crafting.SmithingRecipe;
import net.mayaan.world.item.crafting.SmokingRecipe;
import net.mayaan.world.item.crafting.StonecutterRecipe;

public interface RecipeType<T extends Recipe<?>> {
    public static final RecipeType<CraftingRecipe> CRAFTING = RecipeType.register("crafting");
    public static final RecipeType<SmeltingRecipe> SMELTING = RecipeType.register("smelting");
    public static final RecipeType<BlastingRecipe> BLASTING = RecipeType.register("blasting");
    public static final RecipeType<SmokingRecipe> SMOKING = RecipeType.register("smoking");
    public static final RecipeType<CampfireCookingRecipe> CAMPFIRE_COOKING = RecipeType.register("campfire_cooking");
    public static final RecipeType<StonecutterRecipe> STONECUTTING = RecipeType.register("stonecutting");
    public static final RecipeType<SmithingRecipe> SMITHING = RecipeType.register("smithing");

    public static <T extends Recipe<?>> RecipeType<T> register(final String name) {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, Identifier.withDefaultNamespace(name), new RecipeType<T>(){

            public String toString() {
                return name;
            }
        });
    }
}

