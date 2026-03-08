/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.sounds;

import com.mojang.blaze3d.audio.DeviceList;

public interface DeviceTracker {
    public DeviceList currentDevices();

    public void tick();

    public void forceRefresh();
}

