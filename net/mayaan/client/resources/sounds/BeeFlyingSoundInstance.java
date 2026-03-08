/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources.sounds;

import net.mayaan.client.resources.sounds.AbstractTickableSoundInstance;
import net.mayaan.client.resources.sounds.BeeAggressiveSoundInstance;
import net.mayaan.client.resources.sounds.BeeSoundInstance;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.entity.animal.bee.Bee;

public class BeeFlyingSoundInstance
extends BeeSoundInstance {
    public BeeFlyingSoundInstance(Bee bee) {
        super(bee, SoundEvents.BEE_LOOP, SoundSource.NEUTRAL);
    }

    @Override
    protected AbstractTickableSoundInstance getAlternativeSoundInstance() {
        return new BeeAggressiveSoundInstance(this.bee);
    }

    @Override
    protected boolean shouldSwitchSounds() {
        return this.bee.isAngry();
    }
}

