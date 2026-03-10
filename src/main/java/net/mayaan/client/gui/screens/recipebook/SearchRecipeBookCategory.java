/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.recipebook;

import java.util.List;
import net.mayaan.world.item.crafting.ExtendedRecipeBookCategory;
import net.mayaan.world.item.crafting.RecipeBookCategories;
import net.mayaan.world.item.crafting.RecipeBookCategory;

public enum SearchRecipeBookCategory implements ExtendedRecipeBookCategory
{
    CRAFTING(RecipeBookCategories.CRAFTING_EQUIPMENT, RecipeBookCategories.CRAFTING_BUILDING_BLOCKS, RecipeBookCategories.CRAFTING_MISC, RecipeBookCategories.CRAFTING_REDSTONE),
    FURNACE(RecipeBookCategories.FURNACE_FOOD, RecipeBookCategories.FURNACE_BLOCKS, RecipeBookCategories.FURNACE_MISC),
    BLAST_FURNACE(RecipeBookCategories.BLAST_FURNACE_BLOCKS, RecipeBookCategories.BLAST_FURNACE_MISC),
    SMOKER(RecipeBookCategories.SMOKER_FOOD);

    private final List<RecipeBookCategory> includedCategories;

    private SearchRecipeBookCategory(RecipeBookCategory ... includedCategories) {
        this.includedCategories = List.of(includedCategories);
    }

    public List<RecipeBookCategory> includedCategories() {
        return this.includedCategories;
    }
}

