/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.navigation;

import net.mayaan.core.BlockPos;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.ai.navigation.PathNavigation;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.mayaan.world.level.pathfinder.PathFinder;
import net.mayaan.world.phys.Vec3;

public class AmphibiousPathNavigation
extends PathNavigation {
    public AmphibiousPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new AmphibiousNodeEvaluator(false);
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    @Override
    protected Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), this.mob.getY(0.5), this.mob.getZ());
    }

    @Override
    protected double getGroundY(Vec3 target) {
        return target.y;
    }

    @Override
    protected boolean canMoveDirectly(Vec3 startPos, Vec3 stopPos) {
        if (this.mob.isInLiquid()) {
            return AmphibiousPathNavigation.isClearForMovementBetween(this.mob, startPos, stopPos, false);
        }
        return false;
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        return !this.level.getBlockState(pos.below()).isAir();
    }

    @Override
    public void setCanFloat(boolean canFloat) {
    }

    @Override
    public boolean canNavigateGround() {
        return true;
    }
}

