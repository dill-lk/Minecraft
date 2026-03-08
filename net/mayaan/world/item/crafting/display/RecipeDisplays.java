/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.crafting.display;

import net.mayaan.core.Registry;
import net.mayaan.world.item.crafting.display.FurnaceRecipeDisplay;
import net.mayaan.world.item.crafting.display.RecipeDisplay;
import net.mayaan.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.mayaan.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.mayaan.world.item.crafting.display.SmithingRecipeDisplay;
import net.mayaan.world.item.crafting.display.StonecutterRecipeDisplay;

public class RecipeDisplays {
    public static RecipeDisplay.Type<?> bootstrap(Registry<RecipeDisplay.Type<?>> registry) {
        Registry.register(registry, "crafting_shapeless", ShapelessCraftingRecipeDisplay.TYPE);
        Registry.register(registry, "crafting_shaped", ShapedCraftingRecipeDisplay.TYPE);
        Registry.register(registry, "furnace", FurnaceRecipeDisplay.TYPE);
        Registry.register(registry, "stonecutter", StonecutterRecipeDisplay.TYPE);
        return Registry.register(registry, "smithing", SmithingRecipeDisplay.TYPE);
    }
}

