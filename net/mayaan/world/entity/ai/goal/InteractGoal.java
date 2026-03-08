/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.goal;

import java.util.EnumSet;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.ai.goal.Goal;
import net.mayaan.world.entity.ai.goal.LookAtPlayerGoal;

public class InteractGoal
extends LookAtPlayerGoal {
    public InteractGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance) {
        super(mob, lookAtType, lookDistance);
        this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
    }

    public InteractGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance, float probability) {
        super(mob, lookAtType, lookDistance, probability);
        this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
    }
}

