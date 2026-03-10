/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.Holder;
import net.mayaan.core.particles.ParticleOptions;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.MayaanServer;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundSource;
import net.mayaan.util.RandomSource;
import net.mayaan.world.Difficulty;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.CommonLevelAccessor;
import net.mayaan.world.level.ScheduledTickAccess;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.chunk.ChunkSource;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.level.redstone.NeighborUpdater;
import net.mayaan.world.level.storage.LevelData;
import net.mayaan.world.phys.Vec3;
import net.mayaan.world.ticks.ScheduledTick;
import net.mayaan.world.ticks.TickPriority;
import org.jspecify.annotations.Nullable;

public interface LevelAccessor
extends CommonLevelAccessor,
ScheduledTickAccess {
    public long nextSubTickCount();

    @Override
    default public <T> ScheduledTick<T> createTick(BlockPos pos, T type, int tickDelay, TickPriority priority) {
        return new ScheduledTick<T>(type, pos, this.getGameTime() + (long)tickDelay, priority, this.nextSubTickCount());
    }

    @Override
    default public <T> ScheduledTick<T> createTick(BlockPos pos, T type, int tickDelay) {
        return new ScheduledTick<T>(type, pos, this.getGameTime() + (long)tickDelay, this.nextSubTickCount());
    }

    public LevelData getLevelData();

    default public long getGameTime() {
        return this.getLevelData().getGameTime();
    }

    public @Nullable MayaanServer getServer();

    default public Difficulty getDifficulty() {
        return this.getLevelData().getDifficulty();
    }

    public ChunkSource getChunkSource();

    @Override
    default public boolean hasChunk(int chunkX, int chunkZ) {
        return this.getChunkSource().hasChunk(chunkX, chunkZ);
    }

    public RandomSource getRandom();

    default public void updateNeighborsAt(BlockPos pos, Block sourceBlock) {
    }

    default public void neighborShapeChanged(Direction direction, BlockPos pos, BlockPos neighborPos, BlockState neighborState, @Block.UpdateFlags int updateFlags, int updateLimit) {
        NeighborUpdater.executeShapeUpdate(this, direction, pos, neighborPos, neighborState, updateFlags, updateLimit - 1);
    }

    default public void playSound(@Nullable Entity except, BlockPos pos, SoundEvent soundEvent, SoundSource source) {
        this.playSound(except, pos, soundEvent, source, 1.0f, 1.0f);
    }

    public void playSound(@Nullable Entity var1, BlockPos var2, SoundEvent var3, SoundSource var4, float var5, float var6);

    public void addParticle(ParticleOptions var1, double var2, double var4, double var6, double var8, double var10, double var12);

    public void levelEvent(@Nullable Entity var1, int var2, BlockPos var3, int var4);

    default public void levelEvent(int type, BlockPos pos, int data) {
        this.levelEvent(null, type, pos, data);
    }

    public void gameEvent(Holder<GameEvent> var1, Vec3 var2, GameEvent.Context var3);

    default public void gameEvent(@Nullable Entity sourceEntity, Holder<GameEvent> gameEvent, Vec3 pos) {
        this.gameEvent(gameEvent, pos, new GameEvent.Context(sourceEntity, null));
    }

    default public void gameEvent(@Nullable Entity sourceEntity, Holder<GameEvent> gameEvent, BlockPos pos) {
        this.gameEvent(gameEvent, pos, new GameEvent.Context(sourceEntity, null));
    }

    default public void gameEvent(Holder<GameEvent> gameEvent, BlockPos pos, GameEvent.Context context) {
        this.gameEvent(gameEvent, Vec3.atCenterOf(pos), context);
    }

    default public void gameEvent(ResourceKey<GameEvent> gameEvent, BlockPos pos, GameEvent.Context context) {
        this.gameEvent((Holder<GameEvent>)this.registryAccess().lookupOrThrow(Registries.GAME_EVENT).getOrThrow(gameEvent), pos, context);
    }
}

