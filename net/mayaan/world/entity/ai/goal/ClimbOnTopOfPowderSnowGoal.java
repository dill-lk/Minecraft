/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.goal;

import java.util.EnumSet;
import net.mayaan.core.BlockPos;
import net.mayaan.tags.EntityTypeTags;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.ai.goal.Goal;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.shapes.Shapes;

public class ClimbOnTopOfPowderSnowGoal
extends Goal {
    private final Mob mob;
    private final Level level;

    public ClimbOnTopOfPowderSnowGoal(Mob mob, Level level) {
        this.mob = mob;
        this.level = level;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        boolean inPowderSnow;
        boolean bl = inPowderSnow = this.mob.wasInPowderSnow || this.mob.isInPowderSnow;
        if (!inPowderSnow || !this.mob.is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
            return false;
        }
        BlockPos above = this.mob.blockPosition().above();
        BlockState aboveBlockState = this.level.getBlockState(above);
        return aboveBlockState.is(Blocks.POWDER_SNOW) || aboveBlockState.getCollisionShape(this.level, above) == Shapes.empty();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.mob.getJumpControl().jump();
    }
}

