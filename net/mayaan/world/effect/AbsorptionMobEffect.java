/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.effect;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectCategory;
import net.mayaan.world.entity.LivingEntity;

class AbsorptionMobEffect
extends MobEffect {
    protected AbsorptionMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
        return mob.getAbsorptionAmount() > 0.0f;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
        return true;
    }

    @Override
    public void onEffectStarted(LivingEntity mob, int amplifier) {
        super.onEffectStarted(mob, amplifier);
        mob.setAbsorptionAmount(Math.max(mob.getAbsorptionAmount(), (float)(4 * (1 + amplifier))));
    }
}

