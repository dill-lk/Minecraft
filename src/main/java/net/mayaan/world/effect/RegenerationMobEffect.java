/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.effect;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectCategory;
import net.mayaan.world.entity.LivingEntity;

class RegenerationMobEffect
extends MobEffect {
    protected RegenerationMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
        if (mob.getHealth() < mob.getMaxHealth()) {
            mob.heal(1.0f);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
        int interval = 50 >> amplification;
        if (interval > 0) {
            return tickCount % interval == 0;
        }
        return true;
    }
}

