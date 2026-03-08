/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.projectile.hurtingprojectile.windcharge;

import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.util.random.WeightedList;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.monster.breeze.Breeze;
import net.mayaan.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import net.mayaan.world.level.Level;
import net.mayaan.world.phys.Vec3;

public class BreezeWindCharge
extends AbstractWindCharge {
    private static final float RADIUS = 3.0f;

    public BreezeWindCharge(EntityType<? extends AbstractWindCharge> type, Level level) {
        super(type, level);
    }

    public BreezeWindCharge(Breeze breeze, Level level) {
        super(EntityType.BREEZE_WIND_CHARGE, level, breeze, breeze.getX(), breeze.getFiringYPosition(), breeze.getZ());
    }

    @Override
    protected void explode(Vec3 position) {
        this.level().explode(this, null, EXPLOSION_DAMAGE_CALCULATOR, position.x(), position.y(), position.z(), 3.0f, false, Level.ExplosionInteraction.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, WeightedList.of(), SoundEvents.BREEZE_WIND_CHARGE_BURST);
    }
}

