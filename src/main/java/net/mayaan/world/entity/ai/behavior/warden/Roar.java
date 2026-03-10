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
import net.mayaan.util.Unit;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Pose;
import net.mayaan.world.entity.ai.Brain;
import net.mayaan.world.entity.ai.behavior.Behavior;
import net.mayaan.world.entity.ai.behavior.BehaviorUtils;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.MemoryStatus;
import net.mayaan.world.entity.monster.warden.Warden;
import net.mayaan.world.entity.monster.warden.WardenAi;

public class Roar
extends Behavior<Warden> {
    private static final int TICKS_BEFORE_PLAYING_ROAR_SOUND = 25;
    private static final int ROAR_ANGER_INCREASE = 20;

    public Roar() {
        super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.ROAR_TARGET, (Object)((Object)MemoryStatus.VALUE_PRESENT), MemoryModuleType.ATTACK_TARGET, (Object)((Object)MemoryStatus.VALUE_ABSENT), MemoryModuleType.ROAR_SOUND_COOLDOWN, (Object)((Object)MemoryStatus.REGISTERED), MemoryModuleType.ROAR_SOUND_DELAY, (Object)((Object)MemoryStatus.REGISTERED)), WardenAi.ROAR_DURATION);
    }

    @Override
    protected void start(ServerLevel level, Warden body, long timestamp) {
        Brain<Warden> brain = body.getBrain();
        brain.setMemoryWithExpiry(MemoryModuleType.ROAR_SOUND_DELAY, Unit.INSTANCE, 25L);
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        LivingEntity target = body.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).get();
        BehaviorUtils.lookAtEntity(body, target);
        body.setPose(Pose.ROARING);
        body.increaseAngerAt(target, 20, false);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Warden body, long timestamp) {
        return true;
    }

    @Override
    protected void tick(ServerLevel level, Warden body, long timestamp) {
        if (body.getBrain().hasMemoryValue(MemoryModuleType.ROAR_SOUND_DELAY) || body.getBrain().hasMemoryValue(MemoryModuleType.ROAR_SOUND_COOLDOWN)) {
            return;
        }
        body.getBrain().setMemoryWithExpiry(MemoryModuleType.ROAR_SOUND_COOLDOWN, Unit.INSTANCE, WardenAi.ROAR_DURATION - 25);
        body.playSound(SoundEvents.WARDEN_ROAR, 3.0f, 1.0f);
    }

    @Override
    protected void stop(ServerLevel level, Warden body, long timestamp) {
        if (body.hasPose(Pose.ROARING)) {
            body.setPose(Pose.STANDING);
        }
        body.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).ifPresent(body::setAttackTarget);
        body.getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
    }
}

