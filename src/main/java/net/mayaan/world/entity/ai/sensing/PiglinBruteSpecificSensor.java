/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 */
package net.mayaan.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.ai.Brain;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.mayaan.world.entity.ai.sensing.Sensor;
import net.mayaan.world.entity.boss.wither.WitherBoss;
import net.mayaan.world.entity.monster.piglin.AbstractPiglin;
import net.mayaan.world.entity.monster.piglin.PiglinAi;
import net.mayaan.world.entity.monster.skeleton.WitherSkeleton;

public class PiglinBruteSpecificSensor
extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEARBY_ADULT_PIGLINS);
    }

    @Override
    protected void doTick(ServerLevel level, LivingEntity body) {
        Brain<? extends LivingEntity> brain = body.getBrain();
        NearestVisibleLivingEntities visibleLivingEntities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
        Optional<Mob> nemesis = visibleLivingEntities.findClosest(entity -> entity instanceof WitherSkeleton || entity instanceof WitherBoss).map(Mob.class::cast);
        List<AbstractPiglin> adultPiglins = PiglinAi.findNearbyAdultPiglins(brain);
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, nemesis);
        brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, adultPiglins);
    }
}

