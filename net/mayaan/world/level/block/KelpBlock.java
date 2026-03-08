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
import net.mayaan.tags.BlockTags;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.GrowingPlantHeadBlock;
import net.mayaan.world.level.block.LiquidBlockContainer;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.material.Fluid;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.level.material.Fluids;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class KelpBlock
extends GrowingPlantHeadBlock
implements LiquidBlockContainer {
    public static final MapCodec<KelpBlock> CODEC = KelpBlock.simpleCodec(KelpBlock::new);
    private static final double GROW_PER_TICK_PROBABILITY = 0.14;
    private static final VoxelShape SHAPE = Block.column(16.0, 0.0, 9.0);

    public MapCodec<KelpBlock> codec() {
        return CODEC;
    }

    protected KelpBlock(BlockBehaviour.Properties properties) {
        super(properties, Direction.UP, SHAPE, true, 0.14);
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.is(Blocks.WATER);
    }

    @Override
    protected Block getBodyBlock() {
        return Blocks.KELP_PLANT;
    }

    @Override
    protected boolean canAttachTo(BlockState state) {
        return !state.is(BlockTags.CANNOT_SUPPORT_KELP);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return 1;
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
    protected FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }
}

