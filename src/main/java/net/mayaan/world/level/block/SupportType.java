/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.shapes.BooleanOp;
import net.mayaan.world.phys.shapes.Shapes;
import net.mayaan.world.phys.shapes.VoxelShape;

public enum SupportType {
    FULL{

        @Override
        public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return Block.isFaceFull(state.getBlockSupportShape(level, pos), direction);
        }
    }
    ,
    CENTER{
        private final VoxelShape CENTER_SUPPORT_SHAPE = Block.column(2.0, 0.0, 10.0);

        @Override
        public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return !Shapes.joinIsNotEmpty(state.getBlockSupportShape(level, pos).getFaceShape(direction), this.CENTER_SUPPORT_SHAPE, BooleanOp.ONLY_SECOND);
        }
    }
    ,
    RIGID{
        private final VoxelShape RIGID_SUPPORT_SHAPE = Shapes.join(Shapes.block(), Block.column(12.0, 0.0, 16.0), BooleanOp.ONLY_FIRST);

        @Override
        public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return !Shapes.joinIsNotEmpty(state.getBlockSupportShape(level, pos).getFaceShape(direction), this.RIGID_SUPPORT_SHAPE, BooleanOp.ONLY_SECOND);
        }
    };


    public abstract boolean isSupporting(BlockState var1, BlockGetter var2, BlockPos var3, Direction var4);
}

