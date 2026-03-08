/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.behavior;

import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.Brain;
import net.mayaan.world.entity.ai.behavior.BehaviorControl;
import net.mayaan.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.mayaan.world.entity.raid.Raid;
import net.mayaan.world.entity.schedule.Activity;

public class ResetRaidStatus {
    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create(i -> i.point((level, body, timestamp) -> {
            if (level.getRandom().nextInt(20) != 0) {
                return false;
            }
            Brain<? extends LivingEntity> brain = body.getBrain();
            Raid nearbyRaid = level.getRaidAt(body.blockPosition());
            if (nearbyRaid == null || nearbyRaid.isStopped() || nearbyRaid.isLoss()) {
                brain.setDefaultActivity(Activity.IDLE);
                brain.updateActivityFromSchedule(level.environmentAttributes(), level.getGameTime(), body.position());
            }
            return true;
        }));
    }
}

