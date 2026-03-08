/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world;

import net.mayaan.core.BlockPos;
import net.mayaan.world.WorldlyContainer;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.block.state.BlockState;

public interface WorldlyContainerHolder {
    public WorldlyContainer getContainer(BlockState var1, LevelAccessor var2, BlockPos var3);
}

