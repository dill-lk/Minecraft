/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.inventory;

import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.inventory.AbstractContainerMenu;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface MenuConstructor {
    public @Nullable AbstractContainerMenu createMenu(int var1, Inventory var2, Player var3);
}

