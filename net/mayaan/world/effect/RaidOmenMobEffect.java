/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.effect;

import net.mayaan.core.BlockPos;
import net.mayaan.core.particles.ParticleOptions;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectCategory;
import net.mayaan.world.entity.LivingEntity;

class RaidOmenMobEffect
extends MobEffect {
    protected RaidOmenMobEffect(MobEffectCategory category, int color, ParticleOptions particleOptions) {
        super(category, color, particleOptions);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int remainingDuration, int amplification) {
        return remainingDuration == 1;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
        if (mob instanceof ServerPlayer) {
            BlockPos raidOmenPosition;
            ServerPlayer player = (ServerPlayer)mob;
            if (!mob.isSpectator() && (raidOmenPosition = player.getRaidOmenPosition()) != null) {
                level.getRaids().createOrExtendRaid(player, raidOmenPosition);
                player.clearRaidOmenPosition();
                return false;
            }
        }
        return true;
    }
}

