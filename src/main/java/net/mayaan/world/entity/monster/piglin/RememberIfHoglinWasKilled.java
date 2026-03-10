/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.Applicative
 */
package net.mayaan.world.entity.monster.piglin;

import com.mojang.datafixers.kinds.Applicative;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.BehaviorControl;
import net.mayaan.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.monster.piglin.PiglinAi;

public class RememberIfHoglinWasKilled {
    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create(i -> i.group(i.present(MemoryModuleType.ATTACK_TARGET), i.registered(MemoryModuleType.HUNTED_RECENTLY)).apply((Applicative)i, (attackTarget, huntedRecently) -> (level, body, timestamp) -> {
            LivingEntity target = (LivingEntity)i.get(attackTarget);
            if (target.is(EntityType.HOGLIN) && target.isDeadOrDying()) {
                huntedRecently.setWithExpiry(true, PiglinAi.TIME_BETWEEN_HUNTS.sample(body.level().getRandom()));
            }
            return true;
        }));
    }
}

