/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.inventory;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.entity.player.StackedItemContents;
import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.inventory.MenuType;
import net.mayaan.world.inventory.RecipeBookType;
import net.mayaan.world.item.crafting.RecipeHolder;

public abstract class RecipeBookMenu
extends AbstractContainerMenu {
    public RecipeBookMenu(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    public abstract PostPlaceAction handlePlacement(boolean var1, boolean var2, RecipeHolder<?> var3, ServerLevel var4, Inventory var5);

    public abstract void fillCraftSlotsStackedContents(StackedItemContents var1);

    public abstract RecipeBookType getRecipeBookType();

    public static enum PostPlaceAction {
        NOTHING,
        PLACE_GHOST_RECIPE;

    }
}

