/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.monster.breeze;

import java.util.Map;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.Unit;
import net.mayaan.world.effect.MobEffects;
import net.mayaan.world.entity.ai.behavior.Behavior;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.memory.MemoryStatus;
import net.mayaan.world.entity.monster.breeze.Breeze;

public class ShootWhenStuck
extends Behavior<Breeze> {
    public ShootWhenStuck() {
        super(Map.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.BREEZE_JUMP_INHALING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREEZE_JUMP_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREEZE_SHOOT, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Breeze breeze) {
        return breeze.isPassenger() || breeze.isInWater() || breeze.getEffect(MobEffects.LEVITATION) != null;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Breeze body, long timestamp) {
        return false;
    }

    @Override
    protected void start(ServerLevel level, Breeze breeze, long timestamp) {
        breeze.getBrain().setMemoryWithExpiry(MemoryModuleType.BREEZE_SHOOT, Unit.INSTANCE, 60L);
    }
}

