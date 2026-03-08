/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.item.ItemStack;

public interface Shearable {
    public void shear(ServerLevel var1, SoundSource var2, ItemStack var3);

    public boolean readyForShearing();
}

