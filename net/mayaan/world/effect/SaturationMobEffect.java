/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.effect;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.effect.InstantenousMobEffect;
import net.mayaan.world.effect.MobEffectCategory;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;

class SaturationMobEffect
extends InstantenousMobEffect {
    protected SaturationMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
        if (mob instanceof Player) {
            Player player = (Player)mob;
            player.getFoodData().eat(amplification + 1, 1.0f);
        }
        return true;
    }
}

