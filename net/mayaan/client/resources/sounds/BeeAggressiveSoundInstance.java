/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources.sounds;

import net.mayaan.client.resources.sounds.AbstractTickableSoundInstance;
import net.mayaan.client.resources.sounds.BeeFlyingSoundInstance;
import net.mayaan.client.resources.sounds.BeeSoundInstance;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.entity.animal.bee.Bee;

public class BeeAggressiveSoundInstance
extends BeeSoundInstance {
    public BeeAggressiveSoundInstance(Bee bee) {
        super(bee, SoundEvents.BEE_LOOP_AGGRESSIVE, SoundSource.NEUTRAL);
        this.delay = 0;
    }

    @Override
    protected AbstractTickableSoundInstance getAlternativeSoundInstance() {
        return new BeeFlyingSoundInstance(this.bee);
    }

    @Override
    protected boolean shouldSwitchSounds() {
        return !this.bee.isAngry();
    }
}

