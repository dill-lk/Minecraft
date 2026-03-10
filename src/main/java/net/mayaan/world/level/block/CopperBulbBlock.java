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
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;

public class CopperBulbBlock
extends Block {
    public static final MapCodec<CopperBulbBlock> CODEC = CopperBulbBlock.simpleCodec(CopperBulbBlock::new);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    protected MapCodec<? extends CopperBulbBlock> codec() {
        return CODEC;
    }

    public CopperBulbBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.defaultBlockState().setValue(LIT, false)).setValue(POWERED, false));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (oldState.getBlock() != state.getBlock() && level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            this.checkAndFlip(state, serverLevel, pos);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            this.checkAndFlip(state, serverLevel, pos);
        }
    }

    public void checkAndFlip(BlockState state, ServerLevel level, BlockPos pos) {
        boolean signal = level.hasNeighborSignal(pos);
        if (signal == state.getValue(POWERED)) {
            return;
        }
        BlockState newState = state;
        if (!state.getValue(POWERED).booleanValue()) {
            level.playSound(null, pos, (newState = (BlockState)newState.cycle(LIT)).getValue(LIT) != false ? SoundEvents.COPPER_BULB_TURN_ON : SoundEvents.COPPER_BULB_TURN_OFF, SoundSource.BLOCKS);
        }
        level.setBlock(pos, (BlockState)newState.setValue(POWERED, signal), 3);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, POWERED);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return level.getBlockState(pos).getValue(LIT) != false ? 15 : 0;
    }
}

