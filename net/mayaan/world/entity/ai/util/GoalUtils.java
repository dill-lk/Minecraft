/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.util;

import net.mayaan.core.BlockPos;
import net.mayaan.tags.FluidTags;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.PathfinderMob;
import net.mayaan.world.entity.ai.navigation.PathNavigation;
import net.mayaan.world.level.pathfinder.WalkNodeEvaluator;
import net.mayaan.world.phys.Vec3;

public class GoalUtils {
    public static boolean hasGroundPathNavigation(Mob mob) {
        return mob.getNavigation().canNavigateGround();
    }

    public static boolean mobRestricted(PathfinderMob mob, double horizontalDist) {
        return mob.hasHome() && mob.getHomePosition().closerToCenterThan(mob.position(), (double)mob.getHomeRadius() + horizontalDist + 1.0);
    }

    public static boolean isOutsideLimits(BlockPos pos, PathfinderMob mob) {
        return mob.level().isOutsideBuildHeight(pos.getY());
    }

    public static boolean isRestricted(boolean restrict, PathfinderMob mob, BlockPos pos) {
        return restrict && !mob.isWithinHome(pos);
    }

    public static boolean isRestricted(boolean restrict, PathfinderMob mob, Vec3 pos) {
        return restrict && !mob.isWithinHome(pos);
    }

    public static boolean isNotStable(PathNavigation navigation, BlockPos pos) {
        return !navigation.isStableDestination(pos);
    }

    public static boolean isWater(PathfinderMob mob, BlockPos pos) {
        return mob.level().getFluidState(pos).is(FluidTags.WATER);
    }

    public static boolean hasMalus(PathfinderMob mob, BlockPos pos) {
        return mob.getPathfindingMalus(WalkNodeEvaluator.getPathTypeStatic(mob, pos)) != 0.0f;
    }

    public static boolean isSolid(PathfinderMob mob, BlockPos pos) {
        return mob.level().getBlockState(pos).isSolid();
    }
}

