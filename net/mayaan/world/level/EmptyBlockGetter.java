/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level;

import net.mayaan.core.BlockPos;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

public enum EmptyBlockGetter implements BlockGetter
{
    INSTANCE;


    @Override
    public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
        return null;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}

