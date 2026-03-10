/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 */
package net.mayaan.world.entity.ai.behavior.warden;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.Behavior;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.MemoryStatus;

public class ForceUnmount
extends Behavior<LivingEntity> {
    public ForceUnmount() {
        super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, LivingEntity body) {
        return body.isPassenger();
    }

    @Override
    protected void start(ServerLevel level, LivingEntity body, long timestamp) {
        body.unRide();
    }
}

