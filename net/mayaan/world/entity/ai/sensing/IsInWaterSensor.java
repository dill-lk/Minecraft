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
import net.mayaan.util.Unit;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.sensing.Sensor;

public class IsInWaterSensor
extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.IS_IN_WATER);
    }

    @Override
    protected void doTick(ServerLevel level, LivingEntity body) {
        if (body.isInWater()) {
            body.getBrain().setMemory(MemoryModuleType.IS_IN_WATER, Unit.INSTANCE);
        } else {
            body.getBrain().eraseMemory(MemoryModuleType.IS_IN_WATER);
        }
    }
}

