/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.fog.environment;

import net.mayaan.client.renderer.fog.environment.FogEnvironment;
import net.mayaan.core.Holder;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.level.material.FogType;
import org.jspecify.annotations.Nullable;

public abstract class MobEffectFogEnvironment
extends FogEnvironment {
    public abstract Holder<MobEffect> getMobEffect();

    @Override
    public boolean providesColor() {
        return false;
    }

    @Override
    public boolean modifiesDarkness() {
        return true;
    }

    @Override
    public boolean isApplicable(@Nullable FogType fogType, Entity entity) {
        LivingEntity livingEntity;
        return entity instanceof LivingEntity && (livingEntity = (LivingEntity)entity).hasEffect(this.getMobEffect());
    }
}

