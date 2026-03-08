/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityTicker;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.GameEventListener;
import org.jspecify.annotations.Nullable;

public interface EntityBlock {
    public @Nullable BlockEntity newBlockEntity(BlockPos var1, BlockState var2);

    default public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return null;
    }

    default public <T extends BlockEntity> @Nullable GameEventListener getListener(ServerLevel level, T blockEntity) {
        if (blockEntity instanceof GameEventListener.Provider) {
            GameEventListener.Provider provider = (GameEventListener.Provider)((Object)blockEntity);
            return provider.getListener();
        }
        return null;
    }
}

