/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.animal.golem;

import net.mayaan.sounds.SoundEvent;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.PathfinderMob;
import net.mayaan.world.level.Level;
import org.jspecify.annotations.Nullable;

public abstract class AbstractGolem
extends PathfinderMob {
    protected AbstractGolem(EntityType<? extends AbstractGolem> type, Level level) {
        super((EntityType<? extends PathfinderMob>)type, level);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return false;
    }
}

