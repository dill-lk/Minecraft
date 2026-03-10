/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.entity;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.TrappedChestBlock;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.entity.ChestBlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.redstone.ExperimentalRedstoneUtils;
import net.mayaan.world.level.redstone.Orientation;

public class TrappedChestBlockEntity
extends ChestBlockEntity {
    public TrappedChestBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlockEntityType.TRAPPED_CHEST, worldPosition, blockState);
    }

    @Override
    protected void signalOpenCount(Level level, BlockPos pos, BlockState blockState, int previous, int current) {
        super.signalOpenCount(level, pos, blockState, previous, current);
        if (previous != current) {
            Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, ((Direction)blockState.getValue(TrappedChestBlock.FACING)).getOpposite(), Direction.UP);
            Block block = blockState.getBlock();
            level.updateNeighborsAt(pos, block, orientation);
            level.updateNeighborsAt(pos.below(), block, orientation);
        }
    }
}

