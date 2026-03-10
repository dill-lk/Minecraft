/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.recipebook;

import net.mayaan.world.item.crafting.display.RecipeDisplay;

public interface RecipeUpdateListener {
    public void recipesUpdated();

    public void fillGhostRecipe(RecipeDisplay var1);
}

