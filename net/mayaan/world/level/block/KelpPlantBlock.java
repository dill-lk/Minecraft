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
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.GrowingPlantBodyBlock;
import net.mayaan.world.level.block.GrowingPlantHeadBlock;
import net.mayaan.world.level.block.LiquidBlockContainer;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.material.Fluid;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.level.material.Fluids;
import net.mayaan.world.phys.shapes.Shapes;
import org.jspecify.annotations.Nullable;

public class KelpPlantBlock
extends GrowingPlantBodyBlock
implements LiquidBlockContainer {
    public static final MapCodec<KelpPlantBlock> CODEC = KelpPlantBlock.simpleCodec(KelpPlantBlock::new);

    public MapCodec<KelpPlantBlock> codec() {
        return CODEC;
    }

    protected KelpPlantBlock(BlockBehaviour.Properties properties) {
        super(properties, Direction.UP, Shapes.block(), true);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock)Blocks.KELP;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }

    @Override
    protected boolean canAttachTo(BlockState state) {
        return this.getHeadBlock().canAttachTo(state);
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

