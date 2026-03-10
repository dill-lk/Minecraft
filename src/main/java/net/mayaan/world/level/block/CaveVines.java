/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block;

import java.util.function.ToIntFunction;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.util.Mth;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.level.storage.loot.BuiltInLootTables;
import net.mayaan.world.phys.shapes.VoxelShape;

public interface CaveVines {
    public static final VoxelShape SHAPE = Block.column(14.0, 0.0, 16.0);
    public static final BooleanProperty BERRIES = BlockStateProperties.BERRIES;

    public static InteractionResult use(Entity sourceEntity, BlockState state, Level level, BlockPos pos) {
        if (state.getValue(BERRIES).booleanValue()) {
            if (level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)level;
                Block.dropFromBlockInteractLootTable(serverLevel, BuiltInLootTables.HARVEST_CAVE_VINE, state, level.getBlockEntity(pos), null, sourceEntity, (serverlvl, itemStack) -> Block.popResource((Level)serverlvl, pos, itemStack));
                float pitch = Mth.randomBetween(serverLevel.getRandom(), 0.8f, 1.2f);
                serverLevel.playSound(null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0f, pitch);
                BlockState newState = (BlockState)state.setValue(BERRIES, false);
                serverLevel.setBlock(pos, newState, 2);
                serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(sourceEntity, newState));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static boolean hasGlowBerries(BlockState state) {
        return state.hasProperty(BERRIES) && state.getValue(BERRIES) != false;
    }

    public static ToIntFunction<BlockState> emission(int lightEmission) {
        return state -> state.getValue(BlockStateProperties.BERRIES) != false ? lightEmission : 0;
    }
}

