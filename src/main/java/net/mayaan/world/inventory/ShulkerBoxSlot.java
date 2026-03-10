/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.inventory;

import net.mayaan.world.Container;
import net.mayaan.world.inventory.Slot;
import net.mayaan.world.item.ItemStack;

public class ShulkerBoxSlot
extends Slot {
    public ShulkerBoxSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.getItem().canFitInsideContainerItems();
    }
}

