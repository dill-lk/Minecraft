/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.goal;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Vec3i;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.PathfinderMob;
import net.mayaan.world.entity.ai.goal.Goal;

public class TryFindWaterGoal
extends Goal {
    private final PathfinderMob mob;

    public TryFindWaterGoal(PathfinderMob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return this.mob.onGround() && !this.mob.level().getFluidState(this.mob.blockPosition()).is(FluidTags.WATER);
    }

    @Override
    public void start() {
        Vec3i waterPos = null;
        Iterable<BlockPos> between = BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 2.0), Mth.floor(this.mob.getY() - 2.0), Mth.floor(this.mob.getZ() - 2.0), Mth.floor(this.mob.getX() + 2.0), this.mob.getBlockY(), Mth.floor(this.mob.getZ() + 2.0));
        for (BlockPos pos : between) {
            if (!this.mob.level().getFluidState(pos).is(FluidTags.WATER)) continue;
            waterPos = pos;
            break;
        }
        if (waterPos != null) {
            this.mob.getMoveControl().setWantedPosition(waterPos.getX(), waterPos.getY(), waterPos.getZ(), 1.0);
        }
    }
}

