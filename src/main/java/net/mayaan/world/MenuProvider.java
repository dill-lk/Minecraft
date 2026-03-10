/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world;

import net.mayaan.network.chat.Component;
import net.mayaan.world.inventory.MenuConstructor;

public interface MenuProvider
extends MenuConstructor {
    public Component getDisplayName();
}

