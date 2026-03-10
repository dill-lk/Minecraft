/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block.entity;

import java.util.Objects;
import net.mayaan.core.BlockPos;
import net.mayaan.core.HolderLookup;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.level.BaseSpawner;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.SpawnData;
import net.mayaan.world.level.Spawner;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class SpawnerBlockEntity
extends BlockEntity
implements Spawner {
    private final BaseSpawner spawner = new BaseSpawner(this){
        {
            Objects.requireNonNull(this$0);
        }

        @Override
        public void broadcastEvent(Level level, BlockPos pos, int id) {
            level.blockEvent(pos, Blocks.SPAWNER, id, 0);
        }

        @Override
        public void setNextSpawnData(@Nullable Level level, BlockPos pos, SpawnData nextSpawnData) {
            super.setNextSpawnData(level, pos, nextSpawnData);
            if (level != null) {
                BlockState state = level.getBlockState(pos);
                level.sendBlockUpdated(pos, state, state, 260);
            }
        }
    };

    public SpawnerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlockEntityType.MOB_SPAWNER, worldPosition, blockState);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.spawner.load(this.level, this.worldPosition, input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.spawner.save(output);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, SpawnerBlockEntity entity) {
        entity.spawner.clientTick(level, pos);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SpawnerBlockEntity entity) {
        entity.spawner.serverTick((ServerLevel)level, pos);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = this.saveCustomOnly(registries);
        tag.remove("SpawnPotentials");
        return tag;
    }

    @Override
    public boolean triggerEvent(int b0, int b1) {
        if (this.spawner.onEventTriggered(this.level, b0)) {
            return true;
        }
        return super.triggerEvent(b0, b1);
    }

    @Override
    public void setEntityId(EntityType<?> type, RandomSource random) {
        this.spawner.setEntityId(type, this.level, random, this.worldPosition);
        this.setChanged();
    }

    public BaseSpawner getSpawner() {
        return this.spawner;
    }
}

