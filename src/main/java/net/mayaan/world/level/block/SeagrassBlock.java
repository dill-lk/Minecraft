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
import net.mayaan.server.level.ServerLevel;
import net.mayaan.tags.BlockTags;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.ScheduledTickAccess;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.BonemealableBlock;
import net.mayaan.world.level.block.LiquidBlockContainer;
import net.mayaan.world.level.block.TallSeagrassBlock;
import net.mayaan.world.level.block.VegetationBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.DoubleBlockHalf;
import net.mayaan.world.level.material.Fluid;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.level.material.Fluids;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class SeagrassBlock
extends VegetationBlock
implements BonemealableBlock,
LiquidBlockContainer {
    public static final MapCodec<SeagrassBlock> CODEC = SeagrassBlock.simpleCodec(SeagrassBlock::new);
    private static final VoxelShape SHAPE = Block.column(12.0, 0.0, 12.0);

    public MapCodec<SeagrassBlock> codec() {
        return CODEC;
    }

    protected SeagrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.isFaceSturdy(level, pos, Direction.UP) && !state.is(BlockTags.CANNOT_SUPPORT_SEAGRASS);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        if (fluidState.is(FluidTags.WATER) && fluidState.isFull()) {
            return super.getStateForPlacement(context);
        }
        return null;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        BlockState result = super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
        if (!result.isAir()) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return result;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return level.getBlockState(pos.above()).is(Blocks.WATER);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockState lowerState = Blocks.TALL_SEAGRASS.defaultBlockState();
        BlockState upperState = (BlockState)lowerState.setValue(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER);
        BlockPos above = pos.above();
        level.setBlock(pos, lowerState, 2);
        level.setBlock(above, upperState, 2);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }
}

