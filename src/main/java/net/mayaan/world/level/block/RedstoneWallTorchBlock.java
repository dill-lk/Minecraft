/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.particles.DustParticleOptions;
import net.mayaan.util.RandomSource;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.ScheduledTickAccess;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.HorizontalDirectionalBlock;
import net.mayaan.world.level.block.Mirror;
import net.mayaan.world.level.block.RedstoneTorchBlock;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.WallTorchBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import net.mayaan.world.level.redstone.ExperimentalRedstoneUtils;
import net.mayaan.world.level.redstone.Orientation;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class RedstoneWallTorchBlock
extends RedstoneTorchBlock {
    public static final MapCodec<RedstoneWallTorchBlock> CODEC = RedstoneWallTorchBlock.simpleCodec(RedstoneWallTorchBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public MapCodec<RedstoneWallTorchBlock> codec() {
        return CODEC;
    }

    protected RedstoneWallTorchBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(LIT, true));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return WallTorchBlock.getShape(state);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return WallTorchBlock.canSurvive(level, pos, state.getValue(FACING));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (directionToNeighbour.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = Blocks.WALL_TORCH.getStateForPlacement(context);
        return state == null ? null : (BlockState)this.defaultBlockState().setValue(FACING, state.getValue(FACING));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT).booleanValue()) {
            return;
        }
        Direction opposite = state.getValue(FACING).getOpposite();
        double r = 0.27;
        double x = (double)pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2 + 0.27 * (double)opposite.getStepX();
        double y = (double)pos.getY() + 0.7 + (random.nextDouble() - 0.5) * 0.2 + 0.22;
        double z = (double)pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2 + 0.27 * (double)opposite.getStepZ();
        level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0, 0.0, 0.0);
    }

    @Override
    protected boolean hasNeighborSignal(Level level, BlockPos pos, BlockState state) {
        Direction opposite = state.getValue(FACING).getOpposite();
        return level.hasSignal(pos.relative(opposite), opposite);
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (state.getValue(LIT).booleanValue() && state.getValue(FACING) != direction) {
            return 15;
        }
        return 0;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    protected @Nullable Orientation randomOrientation(Level level, BlockState state) {
        return ExperimentalRedstoneUtils.initialOrientation(level, state.getValue(FACING).getOpposite(), Direction.UP);
    }
}

