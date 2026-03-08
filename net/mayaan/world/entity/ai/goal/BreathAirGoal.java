/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.goal;

import java.util.EnumSet;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Vec3i;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.MoverType;
import net.mayaan.world.entity.PathfinderMob;
import net.mayaan.world.entity.ai.goal.Goal;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.pathfinder.PathComputationType;
import net.mayaan.world.phys.Vec3;

public class BreathAirGoal
extends Goal {
    private final PathfinderMob mob;

    public BreathAirGoal(PathfinderMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.mob.getAirSupply() < 140;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        this.findAirPosition();
    }

    private void findAirPosition() {
        Iterable<BlockPos> between = BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 1.0), this.mob.getBlockY(), Mth.floor(this.mob.getZ() - 1.0), Mth.floor(this.mob.getX() + 1.0), Mth.floor(this.mob.getY() + 8.0), Mth.floor(this.mob.getZ() + 1.0));
        Vec3i destinationPos = null;
        for (BlockPos pos : between) {
            if (!this.givesAir(this.mob.level(), pos)) continue;
            destinationPos = pos;
            break;
        }
        if (destinationPos == null) {
            destinationPos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 8.0, this.mob.getZ());
        }
        this.mob.getNavigation().moveTo(destinationPos.getX(), destinationPos.getY() + 1, destinationPos.getZ(), 1.0);
    }

    @Override
    public void tick() {
        this.findAirPosition();
        this.mob.moveRelative(0.02f, new Vec3(this.mob.xxa, this.mob.yya, this.mob.zza));
        this.mob.move(MoverType.SELF, this.mob.getDeltaMovement());
    }

    private boolean givesAir(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return (level.getFluidState(pos).isEmpty() || state.is(Blocks.BUBBLE_COLUMN)) && state.isPathfindable(PathComputationType.LAND);
    }
}

