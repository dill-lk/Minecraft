/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block;

import java.util.Map;
import java.util.function.Function;
import net.mayaan.core.Direction;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import net.mayaan.world.level.block.state.properties.IntegerProperty;
import net.mayaan.world.phys.shapes.Shapes;
import net.mayaan.world.phys.shapes.VoxelShape;

public interface SegmentableBlock {
    public static final int MIN_SEGMENT = 1;
    public static final int MAX_SEGMENT = 4;
    public static final IntegerProperty AMOUNT = BlockStateProperties.SEGMENT_AMOUNT;

    default public Function<BlockState, VoxelShape> getShapeCalculator(EnumProperty<Direction> facing, IntegerProperty amount) {
        Map<Direction, VoxelShape> shapes = Shapes.rotateHorizontal(Block.box(0.0, 0.0, 0.0, 8.0, this.getShapeHeight(), 8.0));
        return state -> {
            VoxelShape shape = Shapes.empty();
            Direction direction = (Direction)state.getValue(facing);
            int count = state.getValue(amount);
            for (int i = 0; i < count; ++i) {
                shape = Shapes.or(shape, (VoxelShape)shapes.get(direction));
                direction = direction.getCounterClockWise();
            }
            return shape.singleEncompassing();
        };
    }

    default public IntegerProperty getSegmentAmountProperty() {
        return AMOUNT;
    }

    default public double getShapeHeight() {
        return 1.0;
    }

    default public boolean canBeReplaced(BlockState state, BlockPlaceContext context, IntegerProperty segment) {
        return !context.isSecondaryUseActive() && context.getItemInHand().is(state.getBlock().asItem()) && state.getValue(segment) < 4;
    }

    default public BlockState getStateForPlacement(BlockPlaceContext context, Block block, IntegerProperty segment, EnumProperty<Direction> facing) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if (state.is(block)) {
            return (BlockState)state.setValue(segment, Math.min(4, state.getValue(segment) + 1));
        }
        return (BlockState)block.defaultBlockState().setValue(facing, context.getHorizontalDirection().getOpposite());
    }
}

