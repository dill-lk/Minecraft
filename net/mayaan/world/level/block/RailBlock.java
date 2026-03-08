/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.BaseRailBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Mirror;
import net.mayaan.world.level.block.RailState;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import net.mayaan.world.level.block.state.properties.Property;
import net.mayaan.world.level.block.state.properties.RailShape;

public class RailBlock
extends BaseRailBlock {
    public static final MapCodec<RailBlock> CODEC = RailBlock.simpleCodec(RailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE;

    public MapCodec<RailBlock> codec() {
        return CODEC;
    }

    protected RailBlock(BlockBehaviour.Properties properties) {
        super(false, properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(SHAPE, RailShape.NORTH_SOUTH)).setValue(WATERLOGGED, false));
    }

    @Override
    protected void updateState(BlockState state, Level level, BlockPos pos, Block block) {
        if (block.defaultBlockState().isSignalSource() && new RailState(level, pos, state).countPotentialConnections() == 3) {
            this.updateDir(level, pos, state, false);
        }
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        RailShape currentShape = state.getValue(SHAPE);
        RailShape newShape = this.rotate(currentShape, rotation);
        return (BlockState)state.setValue(SHAPE, newShape);
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        RailShape currentShape = state.getValue(SHAPE);
        RailShape newShape = this.mirror(currentShape, mirror);
        return (BlockState)state.setValue(SHAPE, newShape);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, WATERLOGGED);
    }
}

