/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.sensing;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.sensing.Sensor;

public class GolemSensor
extends Sensor<LivingEntity> {
    private static final int GOLEM_SCAN_RATE = 200;
    private static final int MEMORY_TIME_TO_LIVE = 599;

    public GolemSensor() {
        this(200);
    }

    public GolemSensor(int scanRate) {
        super(scanRate);
    }

    @Override
    protected void doTick(ServerLevel level, LivingEntity body) {
        GolemSensor.checkForNearbyGolem(body);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return Set.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.GOLEM_DETECTED_RECENTLY);
    }

    public static void checkForNearbyGolem(LivingEntity body) {
        Optional<List<LivingEntity>> livingEntitiesMemory = body.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
        if (livingEntitiesMemory.isEmpty()) {
            return;
        }
        boolean golemPresent = livingEntitiesMemory.get().stream().anyMatch(entity -> entity.is(EntityType.IRON_GOLEM));
        if (golemPresent) {
            GolemSensor.golemDetected(body);
        }
    }

    public static void golemDetected(LivingEntity body) {
        body.getBrain().setMemoryWithExpiry(MemoryModuleType.GOLEM_DETECTED_RECENTLY, true, 599L);
    }
}

