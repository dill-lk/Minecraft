/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.effect;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectCategory;
import net.mayaan.world.entity.LivingEntity;

public class PoisonMobEffect
extends MobEffect {
    public static final int DAMAGE_INTERVAL = 25;

    protected PoisonMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
        if (mob.getHealth() > 1.0f) {
            mob.hurtServer(level, mob.damageSources().magic(), 1.0f);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
        int interval = 25 >> amplification;
        if (interval > 0) {
            return tickCount % interval == 0;
        }
        return true;
    }
}

