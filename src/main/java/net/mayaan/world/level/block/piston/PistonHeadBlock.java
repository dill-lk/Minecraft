/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block.piston;

import com.mojang.serialization.MapCodec;
import java.util.Map;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.ScheduledTickAccess;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.DirectionalBlock;
import net.mayaan.world.level.block.Mirror;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.piston.PistonBaseBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import net.mayaan.world.level.block.state.properties.PistonType;
import net.mayaan.world.level.pathfinder.PathComputationType;
import net.mayaan.world.level.redstone.ExperimentalRedstoneUtils;
import net.mayaan.world.level.redstone.Orientation;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.Shapes;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class PistonHeadBlock
extends DirectionalBlock {
    public static final MapCodec<PistonHeadBlock> CODEC = PistonHeadBlock.simpleCodec(PistonHeadBlock::new);
    public static final EnumProperty<PistonType> TYPE = BlockStateProperties.PISTON_TYPE;
    public static final BooleanProperty SHORT = BlockStateProperties.SHORT;
    public static final int PLATFORM_THICKNESS = 4;
    private static final VoxelShape SHAPE_PLATFORM = Block.boxZ(16.0, 0.0, 4.0);
    private static final Map<Direction, VoxelShape> SHAPES_SHORT = Shapes.rotateAll(Shapes.or(SHAPE_PLATFORM, Block.boxZ(4.0, 4.0, 16.0)));
    private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(Shapes.or(SHAPE_PLATFORM, Block.boxZ(4.0, 4.0, 20.0)));

    protected MapCodec<PistonHeadBlock> codec() {
        return CODEC;
    }

    public PistonHeadBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(TYPE, PistonType.DEFAULT)).setValue(SHORT, false));
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (state.getValue(SHORT) != false ? SHAPES_SHORT : SHAPES).get(state.getValue(FACING));
    }

    private boolean isFittingBase(BlockState armState, BlockState potentialBase) {
        Block baseBlock = armState.getValue(TYPE) == PistonType.DEFAULT ? Blocks.PISTON : Blocks.STICKY_PISTON;
        return potentialBase.is(baseBlock) && potentialBase.getValue(PistonBaseBlock.EXTENDED) != false && potentialBase.getValue(FACING) == armState.getValue(FACING);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos basePos;
        if (!level.isClientSide() && player.preventsBlockDrops() && this.isFittingBase(state, level.getBlockState(basePos = pos.relative(((Direction)state.getValue(FACING)).getOpposite())))) {
            level.destroyBlock(basePos, false);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        BlockPos basePos = pos.relative(((Direction)state.getValue(FACING)).getOpposite());
        if (this.isFittingBase(state, level.getBlockState(basePos))) {
            level.destroyBlock(basePos, true);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (directionToNeighbour.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState base = level.getBlockState(pos.relative(((Direction)state.getValue(FACING)).getOpposite()));
        return this.isFittingBase(state, base) || base.is(Blocks.MOVING_PISTON) && base.getValue(FACING) == state.getValue(FACING);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (state.canSurvive(level, pos)) {
            level.neighborChanged(pos.relative(((Direction)state.getValue(FACING)).getOpposite()), block, ExperimentalRedstoneUtils.withFront(orientation, ((Direction)state.getValue(FACING)).getOpposite()));
        }
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(state.getValue(TYPE) == PistonType.STICKY ? Blocks.STICKY_PISTON : Blocks.PISTON);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE, SHORT);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}

