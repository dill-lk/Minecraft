/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.behavior;

import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.BehaviorControl;
import net.mayaan.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.mayaan.world.entity.schedule.Activity;

public class WakeUp {
    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create(i -> i.point((level, body, timestamp) -> {
            if (body.getBrain().isActive(Activity.REST) || !body.isSleeping()) {
                return false;
            }
            body.stopSleeping();
            return true;
        }));
    }
}

