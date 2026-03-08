/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.inventory;

import net.mayaan.client.Mayaan;
import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.inventory.ContainerListener;
import net.mayaan.world.item.ItemStack;

public class CreativeInventoryListener
implements ContainerListener {
    private final Mayaan minecraft;

    public CreativeInventoryListener(Mayaan minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {
        this.minecraft.gameMode.handleCreativeModeItemAdd(itemStack, slotIndex);
    }

    @Override
    public void dataChanged(AbstractContainerMenu container, int id, int value) {
    }
}

