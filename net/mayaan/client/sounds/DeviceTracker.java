/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.sounds;

import com.maayanlabs.blaze3d.audio.DeviceList;

public interface DeviceTracker {
    public DeviceList currentDevices();

    public void tick();

    public void forceRefresh();
}

