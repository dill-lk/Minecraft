/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.inventory;

import net.mayaan.world.Container;
import net.mayaan.world.inventory.CrafterMenu;
import net.mayaan.world.inventory.Slot;
import net.mayaan.world.item.ItemStack;

public class CrafterSlot
extends Slot {
    private final CrafterMenu menu;

    public CrafterSlot(Container container, int slot, int x, int y, CrafterMenu menu) {
        super(container, slot, x, y);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return !this.menu.isSlotDisabled(this.index) && super.mayPlace(itemStack);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.menu.slotsChanged(this.container);
    }
}

