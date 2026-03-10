/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Iterables
 */
package net.mayaan.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.EntitySelector;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.mayaan.world.entity.ai.sensing.Sensor;
import net.mayaan.world.entity.monster.breeze.Breeze;

public class BreezeAttackEntitySensor
extends NearestLivingEntitySensor<Breeze> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf((Iterable)Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    @Override
    protected void doTick(ServerLevel level, Breeze breeze) {
        super.doTick(level, breeze);
        breeze.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream).filter(EntitySelector.NO_CREATIVE_OR_SPECTATOR).filter(entity -> Sensor.isEntityAttackable(level, breeze, entity)).findFirst().ifPresentOrElse(entity -> breeze.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, entity), () -> breeze.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));
    }
}

