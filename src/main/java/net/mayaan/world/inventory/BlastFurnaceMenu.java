/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.inventory;

import net.mayaan.world.Container;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.inventory.AbstractFurnaceMenu;
import net.mayaan.world.inventory.ContainerData;
import net.mayaan.world.inventory.MenuType;
import net.mayaan.world.inventory.RecipeBookType;
import net.mayaan.world.item.crafting.RecipePropertySet;
import net.mayaan.world.item.crafting.RecipeType;

public class BlastFurnaceMenu
extends AbstractFurnaceMenu {
    public BlastFurnaceMenu(int containerId, Inventory inventory) {
        super(MenuType.BLAST_FURNACE, RecipeType.BLASTING, RecipePropertySet.BLAST_FURNACE_INPUT, RecipeBookType.BLAST_FURNACE, containerId, inventory);
    }

    public BlastFurnaceMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(MenuType.BLAST_FURNACE, RecipeType.BLASTING, RecipePropertySet.BLAST_FURNACE_INPUT, RecipeBookType.BLAST_FURNACE, containerId, inventory, container, data);
    }
}

