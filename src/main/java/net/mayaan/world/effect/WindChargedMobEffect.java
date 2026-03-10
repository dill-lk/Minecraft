/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.effect;

import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.util.random.WeightedList;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectCategory;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import net.mayaan.world.level.Level;

class WindChargedMobEffect
extends MobEffect {
    protected WindChargedMobEffect(MobEffectCategory category, int color) {
        super(category, color, ParticleTypes.SMALL_GUST);
    }

    @Override
    public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {
        if (reason == Entity.RemovalReason.KILLED) {
            double x = mob.getX();
            double y = mob.getY() + (double)(mob.getBbHeight() / 2.0f);
            double z = mob.getZ();
            float gustStrength = 3.0f + mob.getRandom().nextFloat() * 2.0f;
            level.explode(mob, null, AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR, x, y, z, gustStrength, false, Level.ExplosionInteraction.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, WeightedList.of(), SoundEvents.BREEZE_WIND_CHARGE_BURST);
        }
    }
}

