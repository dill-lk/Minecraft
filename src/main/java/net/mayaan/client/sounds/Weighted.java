/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.sounds;

import net.mayaan.client.sounds.SoundEngine;
import net.mayaan.util.RandomSource;

public interface Weighted<T> {
    public int getWeight();

    public T getSound(RandomSource var1);

    public void preloadIfRequired(SoundEngine var1);
}

