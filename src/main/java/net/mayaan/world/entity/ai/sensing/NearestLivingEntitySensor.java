/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 */
package net.mayaan.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.Brain;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.mayaan.world.entity.ai.sensing.Sensor;
import net.mayaan.world.phys.AABB;

public class NearestLivingEntitySensor<T extends LivingEntity>
extends Sensor<T> {
    @Override
    protected void doTick(ServerLevel level, T body) {
        double followRange = ((LivingEntity)body).getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB boundingBox = ((Entity)body).getBoundingBox().inflate(followRange, followRange, followRange);
        List<LivingEntity> livingEntities = level.getEntitiesOfClass(LivingEntity.class, boundingBox, mob -> mob != body && mob.isAlive());
        livingEntities.sort(Comparator.comparingDouble(arg_0 -> body.distanceToSqr(arg_0)));
        Brain<? extends LivingEntity> brain = ((LivingEntity)body).getBrain();
        brain.setMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES, livingEntities);
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, new NearestVisibleLivingEntities(level, (LivingEntity)body, livingEntities));
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }
}

