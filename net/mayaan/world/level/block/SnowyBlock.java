/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.tags.BlockTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.ScheduledTickAccess;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.BooleanProperty;

public class SnowyBlock
extends Block {
    public static final MapCodec<SnowyBlock> CODEC = SnowyBlock.simpleCodec(SnowyBlock::new);
    public static final BooleanProperty SNOWY = BlockStateProperties.SNOWY;

    protected MapCodec<? extends SnowyBlock> codec() {
        return CODEC;
    }

    protected SnowyBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(SNOWY, false));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (directionToNeighbour == Direction.UP) {
            return (BlockState)state.setValue(SNOWY, SnowyBlock.isSnowySetting(neighbourState));
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState aboveState = context.getLevel().getBlockState(context.getClickedPos().above());
        return (BlockState)this.defaultBlockState().setValue(SNOWY, SnowyBlock.isSnowySetting(aboveState));
    }

    protected static boolean isSnowySetting(BlockState aboveState) {
        return aboveState.is(BlockTags.SNOW);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SNOWY);
    }
}

