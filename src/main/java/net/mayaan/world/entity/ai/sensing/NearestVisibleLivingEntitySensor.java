/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.sensing;

import java.util.Optional;
import java.util.Set;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.mayaan.world.entity.ai.sensing.Sensor;

public abstract class NearestVisibleLivingEntitySensor
extends Sensor<LivingEntity> {
    protected abstract boolean isMatchingEntity(ServerLevel var1, LivingEntity var2, LivingEntity var3);

    protected abstract MemoryModuleType<LivingEntity> getMemoryToSet();

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return Set.of(this.getMemoryToSet(), MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }

    @Override
    protected void doTick(ServerLevel level, LivingEntity body) {
        body.getBrain().setMemory(this.getMemoryToSet(), this.getNearestEntity(level, body));
    }

    private Optional<LivingEntity> getNearestEntity(ServerLevel level, LivingEntity body) {
        return this.getVisibleEntities(body).flatMap(livingEntities -> livingEntities.findClosest(mob -> this.isMatchingEntity(level, body, (LivingEntity)mob)));
    }

    protected Optional<NearestVisibleLivingEntities> getVisibleEntities(LivingEntity body) {
        return body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }
}

