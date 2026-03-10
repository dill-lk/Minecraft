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
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.BaseEntityBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityTicker;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.entity.TrialSpawnerBlockEntity;
import net.mayaan.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import org.jspecify.annotations.Nullable;

public class TrialSpawnerBlock
extends BaseEntityBlock {
    public static final MapCodec<TrialSpawnerBlock> CODEC = TrialSpawnerBlock.simpleCodec(TrialSpawnerBlock::new);
    public static final EnumProperty<TrialSpawnerState> STATE = BlockStateProperties.TRIAL_SPAWNER_STATE;
    public static final BooleanProperty OMINOUS = BlockStateProperties.OMINOUS;

    public MapCodec<TrialSpawnerBlock> codec() {
        return CODEC;
    }

    public TrialSpawnerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(STATE, TrialSpawnerState.INACTIVE)).setValue(OMINOUS, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE, OMINOUS);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new TrialSpawnerBlockEntity(worldPosition, blockState);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        BlockEntityTicker<T> blockEntityTicker;
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            blockEntityTicker = TrialSpawnerBlock.createTickerHelper(type, BlockEntityType.TRIAL_SPAWNER, (innerLevel, pos, state, entity) -> entity.getTrialSpawner().tickServer(serverLevel, pos, state.getOptionalValue(BlockStateProperties.OMINOUS).orElse(false)));
        } else {
            blockEntityTicker = TrialSpawnerBlock.createTickerHelper(type, BlockEntityType.TRIAL_SPAWNER, (innerLevel, pos, state, entity) -> entity.getTrialSpawner().tickClient(innerLevel, pos, state.getOptionalValue(BlockStateProperties.OMINOUS).orElse(false)));
        }
        return blockEntityTicker;
    }
}

