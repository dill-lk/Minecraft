/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.ai.goal;

import net.mayaan.core.BlockPos;
import net.mayaan.core.SectionPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.PathfinderMob;
import net.mayaan.world.entity.ai.behavior.BehaviorUtils;
import net.mayaan.world.entity.ai.goal.RandomStrollGoal;
import net.mayaan.world.entity.ai.util.DefaultRandomPos;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class MoveBackToVillageGoal
extends RandomStrollGoal {
    private static final int MAX_XZ_DIST = 10;
    private static final int MAX_Y_DIST = 7;

    public MoveBackToVillageGoal(PathfinderMob mob, double speedModifier, boolean checkNoActionTime) {
        super(mob, speedModifier, 10, checkNoActionTime);
    }

    @Override
    public boolean canUse() {
        BlockPos pos;
        ServerLevel level = (ServerLevel)this.mob.level();
        if (level.isVillage(pos = this.mob.blockPosition())) {
            return false;
        }
        return super.canUse();
    }

    @Override
    protected @Nullable Vec3 getPosition() {
        BlockPos pos;
        SectionPos sectionPos;
        ServerLevel level = (ServerLevel)this.mob.level();
        SectionPos optimalSectionPos = BehaviorUtils.findSectionClosestToVillage(level, sectionPos = SectionPos.of(pos = this.mob.blockPosition()), 2);
        if (optimalSectionPos != sectionPos) {
            return DefaultRandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf(optimalSectionPos.center()), 1.5707963705062866);
        }
        return null;
    }
}

