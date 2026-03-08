/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world;

import net.mayaan.network.chat.Component;
import net.mayaan.world.MenuProvider;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.inventory.MenuConstructor;

public final class SimpleMenuProvider
implements MenuProvider {
    private final Component title;
    private final MenuConstructor menuConstructor;

    public SimpleMenuProvider(MenuConstructor menuConstructor, Component title) {
        this.menuConstructor = menuConstructor;
        this.title = title;
    }

    @Override
    public Component getDisplayName() {
        return this.title;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return this.menuConstructor.createMenu(containerId, inventory, player);
    }
}

