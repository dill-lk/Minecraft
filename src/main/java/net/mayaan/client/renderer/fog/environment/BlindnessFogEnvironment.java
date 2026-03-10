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

public class BlindnessFogEnvironment
extends MobEffectFogEnvironment {
    @Override
    public Holder<MobEffect> getMobEffect() {
        return MobEffects.BLINDNESS;
    }

    @Override
    public void setupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
        LivingEntity livingEntity;
        MobEffectInstance effect;
        Entity entity = camera.entity();
        if (entity instanceof LivingEntity && (effect = (livingEntity = (LivingEntity)entity).getEffect(this.getMobEffect())) != null) {
            float distance = effect.isInfiniteDuration() ? 5.0f : Mth.lerp(Math.min(1.0f, (float)effect.getDuration() / 20.0f), renderDistance, 5.0f);
            fog.environmentalStart = distance * 0.25f;
            fog.environmentalEnd = distance;
            fog.skyEnd = distance * 0.8f;
            fog.cloudEnd = distance * 0.8f;
        }
    }

    @Override
    public float getModifiedDarkness(LivingEntity entity, float darkness, float partialTickTime) {
        MobEffectInstance instance = entity.getEffect(this.getMobEffect());
        if (instance != null) {
            darkness = instance.endsWithin(19) ? Math.max((float)instance.getDuration() / 20.0f, darkness) : 1.0f;
        }
        return darkness;
    }
}

