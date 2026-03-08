/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.inventory;

import java.util.List;
import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.inventory.RemoteSlot;
import net.mayaan.world.item.ItemStack;

public interface ContainerSynchronizer {
    public void sendInitialData(AbstractContainerMenu var1, List<ItemStack> var2, ItemStack var3, int[] var4);

    public void sendSlotChange(AbstractContainerMenu var1, int var2, ItemStack var3);

    public void sendCarriedChange(AbstractContainerMenu var1, ItemStack var2);

    public void sendDataChange(AbstractContainerMenu var1, int var2, int var3);

    public RemoteSlot createSlot();
}

