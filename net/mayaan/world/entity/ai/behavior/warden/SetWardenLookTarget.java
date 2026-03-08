/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.Applicative
 */
package net.mayaan.world.entity.ai.behavior.warden;

import com.mojang.datafixers.kinds.Applicative;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.BehaviorControl;
import net.mayaan.world.entity.ai.behavior.BlockPosTracker;
import net.mayaan.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;

public class SetWardenLookTarget {
    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create(i -> i.group(i.registered(MemoryModuleType.LOOK_TARGET), i.registered(MemoryModuleType.DISTURBANCE_LOCATION), i.registered(MemoryModuleType.ROAR_TARGET), i.absent(MemoryModuleType.ATTACK_TARGET)).apply((Applicative)i, (lookTarget, disturbance, roarTarget, attackTarget) -> (level, body, timestamp) -> {
            Optional<BlockPos> target = i.tryGet(roarTarget).map(Entity::blockPosition).or(() -> i.tryGet(disturbance));
            if (target.isEmpty()) {
                return false;
            }
            lookTarget.set(new BlockPosTracker(target.get()));
            return true;
        }));
    }
}

