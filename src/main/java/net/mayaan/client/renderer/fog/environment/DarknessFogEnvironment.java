/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.fog.environment;

import net.mayaan.client.Camera;
import net.mayaan.client.DeltaTracker;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.fog.FogData;
import net.mayaan.client.renderer.fog.environment.MobEffectFogEnvironment;
import net.mayaan.core.Holder;
import net.mayaan.util.Mth;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.effect.MobEffects;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;

public class DarknessFogEnvironment
extends MobEffectFogEnvironment {
    @Override
    public Holder<MobEffect> getMobEffect() {
        return MobEffects.DARKNESS;
    }

    @Override
    public void setupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
        LivingEntity livingEntity;
        MobEffectInstance effect;
        Entity entity = camera.entity();
        if (entity instanceof LivingEntity && (effect = (livingEntity = (LivingEntity)entity).getEffect(this.getMobEffect())) != null) {
            float distance = Mth.lerp(effect.getBlendFactor(livingEntity, deltaTracker.getGameTimeDeltaPartialTick(false)), renderDistance, 15.0f);
            fog.environmentalStart = distance * 0.75f;
            fog.environmentalEnd = distance;
            fog.skyEnd = distance;
            fog.cloudEnd = distance;
        }
    }

    @Override
    public float getModifiedDarkness(LivingEntity entity, float darkness, float partialTickTime) {
        MobEffectInstance instance = entity.getEffect(this.getMobEffect());
        return instance != null ? Math.max(instance.getBlendFactor(entity, partialTickTime), darkness) : darkness;
    }
}

