/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 */
package net.mayaan.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.Brain;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.sensing.Sensor;

public class HurtBySensor
extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY);
    }

    @Override
    protected void doTick(ServerLevel level, LivingEntity body) {
        Brain<? extends LivingEntity> brain = body.getBrain();
        DamageSource damageSource = body.getLastDamageSource();
        if (damageSource != null) {
            brain.setMemory(MemoryModuleType.HURT_BY, body.getLastDamageSource());
            Entity entitySource = damageSource.getEntity();
            if (entitySource instanceof LivingEntity) {
                brain.setMemory(MemoryModuleType.HURT_BY_ENTITY, (LivingEntity)entitySource);
            }
        } else {
            brain.eraseMemory(MemoryModuleType.HURT_BY);
        }
        brain.getMemory(MemoryModuleType.HURT_BY_ENTITY).ifPresent(hurtByEntity -> {
            if (!hurtByEntity.isAlive() || hurtByEntity.level() != level) {
                brain.eraseMemory(MemoryModuleType.HURT_BY_ENTITY);
            }
        });
    }
}

