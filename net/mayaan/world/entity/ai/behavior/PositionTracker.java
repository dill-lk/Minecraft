/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.behavior;

import net.mayaan.core.BlockPos;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.phys.Vec3;

public interface PositionTracker {
    public Vec3 currentPosition();

    public BlockPos currentBlockPosition();

    public boolean isVisibleBy(LivingEntity var1);
}

