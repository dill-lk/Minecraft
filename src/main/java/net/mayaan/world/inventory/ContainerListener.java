/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.inventory;

import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.item.ItemStack;

public interface ContainerListener {
    public void slotChanged(AbstractContainerMenu var1, int var2, ItemStack var3);

    public void dataChanged(AbstractContainerMenu var1, int var2, int var3);
}

