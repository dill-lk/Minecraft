/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.effect;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.Difficulty;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectCategory;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.effect.MobEffects;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.raid.Raid;

class BadOmenMobEffect
extends MobEffect {
    protected BadOmenMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int remainingDuration, int amplification) {
        return true;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
        Raid raid;
        ServerPlayer player;
        if (mob instanceof ServerPlayer && !(player = (ServerPlayer)mob).isSpectator() && level.getDifficulty() != Difficulty.PEACEFUL && level.isVillage(player.blockPosition()) && ((raid = level.getRaidAt(player.blockPosition())) == null || raid.getRaidOmenLevel() < raid.getMaxRaidOmenLevel())) {
            player.addEffect(new MobEffectInstance(MobEffects.RAID_OMEN, 600, amplification));
            player.setRaidOmenPosition(player.blockPosition());
            return false;
        }
        return true;
    }
}

