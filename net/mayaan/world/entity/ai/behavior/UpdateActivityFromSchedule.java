/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.behavior;

import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.BehaviorControl;
import net.mayaan.world.entity.ai.behavior.declarative.BehaviorBuilder;

public class UpdateActivityFromSchedule {
    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create(i -> i.point((level, body, timestamp) -> {
            body.getBrain().updateActivityFromSchedule(level.environmentAttributes(), level.getGameTime(), body.position());
            return true;
        }));
    }
}

