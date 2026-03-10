/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources.sounds;

import net.mayaan.client.resources.sounds.AbstractTickableSoundInstance;
import net.mayaan.client.resources.sounds.SoundInstance;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.entity.animal.sniffer.Sniffer;

public class SnifferSoundInstance
extends AbstractTickableSoundInstance {
    private static final float VOLUME = 1.0f;
    private static final float PITCH = 1.0f;
    private final Sniffer sniffer;

    public SnifferSoundInstance(Sniffer sniffer) {
        super(SoundEvents.SNIFFER_DIGGING, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.sniffer = sniffer;
        this.attenuation = SoundInstance.Attenuation.LINEAR;
        this.looping = false;
        this.delay = 0;
    }

    @Override
    public boolean canPlaySound() {
        return !this.sniffer.isSilent();
    }

    @Override
    public void tick() {
        if (this.sniffer.isRemoved() || this.sniffer.getTarget() != null || !this.sniffer.canPlayDiggingSound()) {
            this.stop();
            return;
        }
        this.x = (float)this.sniffer.getX();
        this.y = (float)this.sniffer.getY();
        this.z = (float)this.sniffer.getZ();
        this.volume = 1.0f;
        this.pitch = 1.0f;
    }
}

