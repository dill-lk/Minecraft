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
import net.mayaan.sounds.SoundEvents;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.Pose;
import net.mayaan.world.entity.ai.behavior.Behavior;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.MemoryStatus;
import net.mayaan.world.entity.monster.warden.Warden;

public class Emerging<E extends Warden>
extends Behavior<E> {
    public Emerging(int ticks) {
        super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.IS_EMERGING, (Object)((Object)MemoryStatus.VALUE_PRESENT), MemoryModuleType.WALK_TARGET, (Object)((Object)MemoryStatus.VALUE_ABSENT), MemoryModuleType.LOOK_TARGET, (Object)((Object)MemoryStatus.REGISTERED)), ticks);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, E body, long timestamp) {
        return true;
    }

    @Override
    protected void start(ServerLevel level, E body, long timestamp) {
        ((Entity)body).setPose(Pose.EMERGING);
        ((Entity)body).playSound(SoundEvents.WARDEN_EMERGE, 5.0f, 1.0f);
    }

    @Override
    protected void stop(ServerLevel level, E body, long timestamp) {
        if (((Entity)body).hasPose(Pose.EMERGING)) {
            ((Entity)body).setPose(Pose.STANDING);
        }
    }
}

