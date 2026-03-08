/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.crafting;

import net.mayaan.world.item.crafting.CraftingBookCategory;
import net.mayaan.world.item.crafting.CraftingRecipe;
import net.mayaan.world.item.crafting.PlacementInfo;
import net.mayaan.world.item.crafting.RecipeSerializer;

public abstract class CustomRecipe
implements CraftingRecipe {
    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public abstract RecipeSerializer<? extends CustomRecipe> getSerializer();
}

