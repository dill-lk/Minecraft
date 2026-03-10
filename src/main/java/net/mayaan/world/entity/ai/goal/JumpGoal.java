/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.goal;

import java.util.EnumSet;
import net.mayaan.world.entity.ai.goal.Goal;

public abstract class JumpGoal
extends Goal {
    public JumpGoal() {
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }
}

