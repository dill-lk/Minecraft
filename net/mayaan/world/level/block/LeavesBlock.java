/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.OptionalInt;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.Vec3i;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.tags.BlockTags;
import net.mayaan.util.ParticleUtils;
import net.mayaan.util.RandomSource;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.ScheduledTickAccess;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.SimpleWaterloggedBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.level.block.state.properties.IntegerProperty;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.level.material.Fluids;
import net.mayaan.world.phys.shapes.Shapes;
import net.mayaan.world.phys.shapes.VoxelShape;

public abstract class LeavesBlock
extends Block
implements SimpleWaterloggedBlock {
    public static final int DECAY_DISTANCE = 7;
    public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected final float leafParticleChance;
    private static final int TICK_DELAY = 1;
    private static boolean cutoutLeaves = true;

    public abstract MapCodec<? extends LeavesBlock> codec();

    public LeavesBlock(float leafParticleChance, BlockBehaviour.Properties properties) {
        super(properties);
        this.leafParticleChance = leafParticleChance;
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(DISTANCE, 7)).setValue(PERSISTENT, false)).setValue(WATERLOGGED, false));
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
        if (!cutoutLeaves && neighborState.getBlock() instanceof LeavesBlock) {
            return true;
        }
        return super.skipRendering(state, neighborState, direction);
    }

    public static void setCutoutLeaves(boolean cutoutLeaves) {
        LeavesBlock.cutoutLeaves = cutoutLeaves;
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(DISTANCE) == 7 && state.getValue(PERSISTENT) == false;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.decaying(state)) {
            LeavesBlock.dropResources(state, level, pos);
            level.removeBlock(pos, false);
        }
    }

    protected boolean decaying(BlockState state) {
        return state.getValue(PERSISTENT) == false && state.getValue(DISTANCE) == 7;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, LeavesBlock.updateDistance(state, level, pos), 3);
    }

    @Override
    protected int getLightDampening(BlockState state) {
        return 1;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        int distanceFromNeighbor;
        if (state.getValue(WATERLOGGED).booleanValue()) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if ((distanceFromNeighbor = LeavesBlock.getDistanceAt(neighbourState) + 1) != 1 || state.getValue(DISTANCE) != distanceFromNeighbor) {
            ticks.scheduleTick(pos, this, 1);
        }
        return state;
    }

    private static BlockState updateDistance(BlockState state, LevelAccessor level, BlockPos pos) {
        int newDistance = 7;
        BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            neighborPos.setWithOffset((Vec3i)pos, direction);
            newDistance = Math.min(newDistance, LeavesBlock.getDistanceAt(level.getBlockState(neighborPos)) + 1);
            if (newDistance == 1) break;
        }
        return (BlockState)state.setValue(DISTANCE, newDistance);
    }

    private static int getDistanceAt(BlockState state) {
        return LeavesBlock.getOptionalDistanceAt(state).orElse(7);
    }

    public static OptionalInt getOptionalDistanceAt(BlockState state) {
        if (state.is(BlockTags.PREVENTS_NEARBY_LEAF_DECAY)) {
            return OptionalInt.of(0);
        }
        if (state.hasProperty(DISTANCE)) {
            return OptionalInt.of(state.getValue(DISTANCE));
        }
        return OptionalInt.empty();
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED).booleanValue()) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(state);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);
        LeavesBlock.makeDrippingWaterParticles(level, pos, random, belowState, below);
        this.makeFallingLeavesParticles(level, pos, random, belowState, below);
    }

    private static void makeDrippingWaterParticles(Level level, BlockPos pos, RandomSource random, BlockState belowState, BlockPos below) {
        if (!level.isRainingAt(pos.above())) {
            return;
        }
        if (random.nextInt(15) != 1) {
            return;
        }
        if (belowState.canOcclude() && belowState.isFaceSturdy(level, below, Direction.UP)) {
            return;
        }
        ParticleUtils.spawnParticleBelow(level, pos, random, ParticleTypes.DRIPPING_WATER);
    }

    private void makeFallingLeavesParticles(Level level, BlockPos pos, RandomSource random, BlockState belowState, BlockPos below) {
        if (random.nextFloat() >= this.leafParticleChance) {
            return;
        }
        if (LeavesBlock.isFaceFull(belowState.getCollisionShape(level, below), Direction.UP)) {
            return;
        }
        this.spawnFallingLeavesParticle(level, pos, random);
    }

    protected abstract void spawnFallingLeavesParticle(Level var1, BlockPos var2, RandomSource var3);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockState state = (BlockState)((BlockState)this.defaultBlockState().setValue(PERSISTENT, true)).setValue(WATERLOGGED, replacedFluidState.is(Fluids.WATER));
        return LeavesBlock.updateDistance(state, context.getLevel(), context.getClickedPos());
    }
}

