/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.resources.sounds;

import net.mayaan.client.resources.sounds.Sound;
import net.mayaan.client.sounds.SoundManager;
import net.mayaan.client.sounds.WeighedSoundEvents;
import net.mayaan.resources.Identifier;
import net.mayaan.sounds.SoundSource;
import net.mayaan.util.RandomSource;
import org.jspecify.annotations.Nullable;

public interface SoundInstance {
    public Identifier getIdentifier();

    public @Nullable WeighedSoundEvents resolve(SoundManager var1);

    public @Nullable Sound getSound();

    public SoundSource getSource();

    public boolean isLooping();

    public boolean isRelative();

    public int getDelay();

    public float getVolume();

    public float getPitch();

    public double getX();

    public double getY();

    public double getZ();

    public Attenuation getAttenuation();

    default public boolean canStartSilent() {
        return false;
    }

    default public boolean canPlaySound() {
        return true;
    }

    public static RandomSource createUnseededRandom() {
        return RandomSource.create();
    }

    public static enum Attenuation {
        NONE,
        LINEAR;

    }
}

