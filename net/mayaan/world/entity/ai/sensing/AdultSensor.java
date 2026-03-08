/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 */
package net.mayaan.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import java.util.Set;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.mayaan.world.entity.ai.sensing.Sensor;

public class AdultSensor
extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }

    @Override
    protected void doTick(ServerLevel level, LivingEntity body) {
        body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).ifPresent(livingEntities -> this.setNearestVisibleAdult(body, (NearestVisibleLivingEntities)livingEntities));
    }

    protected void setNearestVisibleAdult(LivingEntity body, NearestVisibleLivingEntities visibleLivingEntities) {
        Optional<LivingEntity> adult = visibleLivingEntities.findClosest(entity -> entity.getType() == body.getType() && !entity.isBaby());
        body.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, adult);
    }
}

