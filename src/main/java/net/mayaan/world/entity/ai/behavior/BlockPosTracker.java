/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.behavior;

import net.mayaan.core.BlockPos;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.behavior.PositionTracker;
import net.mayaan.world.phys.Vec3;

public class BlockPosTracker
implements PositionTracker {
    private final BlockPos blockPos;
    private final Vec3 centerPosition;

    public BlockPosTracker(BlockPos blockPos) {
        this.blockPos = blockPos.immutable();
        this.centerPosition = Vec3.atCenterOf(blockPos);
    }

    public BlockPosTracker(Vec3 vec) {
        this.blockPos = BlockPos.containing(vec);
        this.centerPosition = vec;
    }

    @Override
    public Vec3 currentPosition() {
        return this.centerPosition;
    }

    @Override
    public BlockPos currentBlockPosition() {
        return this.blockPos;
    }

    @Override
    public boolean isVisibleBy(LivingEntity body) {
        return true;
    }

    public String toString() {
        return "BlockPosTracker{blockPos=" + String.valueOf(this.blockPos) + ", centerPosition=" + String.valueOf(this.centerPosition) + "}";
    }
}

