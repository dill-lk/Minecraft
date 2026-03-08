/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.inventory;

import net.mayaan.world.inventory.AbstractContainerMenu;

public interface MenuAccess<T extends AbstractContainerMenu> {
    public T getMenu();
}

