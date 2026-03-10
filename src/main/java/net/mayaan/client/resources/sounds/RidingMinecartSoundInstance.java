/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources.sounds;

import net.mayaan.client.resources.sounds.RidingEntitySoundInstance;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.vehicle.minecart.AbstractMinecart;
import net.mayaan.world.entity.vehicle.minecart.NewMinecartBehavior;

public class RidingMinecartSoundInstance
extends RidingEntitySoundInstance {
    private final Player player;
    private final AbstractMinecart minecart;
    private final boolean underwaterSound;

    public RidingMinecartSoundInstance(Player player, AbstractMinecart minecart, boolean underwaterSound, SoundEvent soundEvent, float volumeMin, float volumeMax, float volumeAmplifier) {
        super(player, minecart, underwaterSound, soundEvent, SoundSource.NEUTRAL, volumeMin, volumeMax, volumeAmplifier);
        this.player = player;
        this.minecart = minecart;
        this.underwaterSound = underwaterSound;
    }

    @Override
    protected boolean shouldNotPlayUnderwaterSound() {
        return this.underwaterSound != this.player.isUnderWater();
    }

    @Override
    protected float getEntitySpeed() {
        return (float)this.minecart.getDeltaMovement().horizontalDistance();
    }

    @Override
    protected boolean shoudlPlaySound() {
        return this.minecart.isOnRails() || !(this.minecart.getBehavior() instanceof NewMinecartBehavior);
    }
}

