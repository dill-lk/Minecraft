/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources.sounds;

import net.mayaan.client.resources.sounds.SoundInstance;

public interface TickableSoundInstance
extends SoundInstance {
    public boolean isStopped();

    public void tick();
}

