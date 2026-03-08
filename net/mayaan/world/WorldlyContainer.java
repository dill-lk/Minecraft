/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world;

import net.mayaan.core.Direction;
import net.mayaan.world.Container;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public interface WorldlyContainer
extends Container {
    public int[] getSlotsForFace(Direction var1);

    public boolean canPlaceItemThroughFace(int var1, ItemStack var2, @Nullable Direction var3);

    public boolean canTakeItemThroughFace(int var1, ItemStack var2, Direction var3);
}

