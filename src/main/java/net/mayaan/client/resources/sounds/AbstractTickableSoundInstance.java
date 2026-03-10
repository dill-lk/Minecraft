/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources.sounds;

import net.mayaan.client.resources.sounds.AbstractSoundInstance;
import net.mayaan.client.resources.sounds.TickableSoundInstance;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundSource;
import net.mayaan.util.RandomSource;

public abstract class AbstractTickableSoundInstance
extends AbstractSoundInstance
implements TickableSoundInstance {
    private boolean stopped;

    protected AbstractTickableSoundInstance(SoundEvent event, SoundSource source, RandomSource random) {
        super(event, source, random);
    }

    @Override
    public boolean isStopped() {
        return this.stopped;
    }

    protected final void stop() {
        this.stopped = true;
        this.looping = false;
    }
}

