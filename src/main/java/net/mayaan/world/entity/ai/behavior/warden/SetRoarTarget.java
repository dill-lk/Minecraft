/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.Applicative
 */
package net.mayaan.world.entity.ai.behavior.warden;

import com.mojang.datafixers.kinds.Applicative;
import java.util.Optional;
import java.util.function.Function;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.BehaviorControl;
import net.mayaan.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.monster.warden.Warden;

public class SetRoarTarget {
    public static <E extends Warden> BehaviorControl<E> create(Function<E, Optional<? extends LivingEntity>> targetFinderFunction) {
        return BehaviorBuilder.create((BehaviorBuilder.Instance<E> i) -> i.group(i.absent(MemoryModuleType.ROAR_TARGET), i.absent(MemoryModuleType.ATTACK_TARGET), i.registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)).apply((Applicative)i, (roarTarget, attackTarget, cantReachSince) -> (level, body, timestamp) -> {
            Optional target = (Optional)targetFinderFunction.apply(body);
            if (target.filter(body::canTargetEntity).isEmpty()) {
                return false;
            }
            roarTarget.set((LivingEntity)target.get());
            cantReachSince.erase();
            return true;
        }));
    }
}

