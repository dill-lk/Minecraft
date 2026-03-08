/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.effect;

import java.util.List;
import net.mayaan.core.Holder;
import net.mayaan.network.chat.Component;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.util.Mth;
import net.mayaan.util.StringUtil;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.effect.MobEffects;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public final class MobEffectUtil {
    public static Component formatDuration(MobEffectInstance instance, float scale, float tickrate) {
        if (instance.isInfiniteDuration()) {
            return Component.translatable("effect.duration.infinite");
        }
        int duration = Mth.floor((float)instance.getDuration() * scale);
        return Component.literal(StringUtil.formatTickDuration(duration, tickrate));
    }

    public static boolean hasDigSpeed(LivingEntity mob) {
        return mob.hasEffect(MobEffects.HASTE) || mob.hasEffect(MobEffects.CONDUIT_POWER);
    }

    public static int getDigSpeedAmplification(LivingEntity mob) {
        int a = 0;
        int b = 0;
        if (mob.hasEffect(MobEffects.HASTE)) {
            a = mob.getEffect(MobEffects.HASTE).getAmplifier();
        }
        if (mob.hasEffect(MobEffects.CONDUIT_POWER)) {
            b = mob.getEffect(MobEffects.CONDUIT_POWER).getAmplifier();
        }
        return Math.max(a, b);
    }

    public static boolean hasWaterBreathing(LivingEntity mob) {
        return mob.hasEffect(MobEffects.WATER_BREATHING) || mob.hasEffect(MobEffects.CONDUIT_POWER) || mob.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS);
    }

    public static boolean shouldEffectsRefillAirsupply(LivingEntity mob) {
        return !mob.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS) || mob.hasEffect(MobEffects.WATER_BREATHING) || mob.hasEffect(MobEffects.CONDUIT_POWER);
    }

    public static List<ServerPlayer> addEffectToPlayersAround(ServerLevel level, @Nullable Entity source, Vec3 position, double radius, MobEffectInstance effectInstance, int displayEffectLimit) {
        Holder<MobEffect> effect = effectInstance.getEffect();
        List<ServerPlayer> players = level.getPlayers(input -> !(!input.gameMode.isSurvival() || source != null && source.isAlliedTo((Entity)input) || !position.closerThan(input.position(), radius) || input.hasEffect(effect) && input.getEffect(effect).getAmplifier() >= effectInstance.getAmplifier() && !input.getEffect(effect).endsWithin(displayEffectLimit - 1)));
        players.forEach(player -> player.addEffect(new MobEffectInstance(effectInstance), source));
        return players;
    }
}

