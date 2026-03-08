/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  it.unimi.dsi.fastutil.longs.LongOpenHashSet
 *  it.unimi.dsi.fastutil.longs.LongSet
 *  org.slf4j.Logger
 */
package net.mayaan.world.level.chunk.storage;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.mayaan.core.HolderLookup;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.nbt.ListTag;
import net.mayaan.nbt.NbtUtils;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.ProblemReporter;
import net.mayaan.util.thread.ConsecutiveExecutor;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.chunk.ChunkAccess;
import net.mayaan.world.level.chunk.storage.IOWorker;
import net.mayaan.world.level.chunk.storage.SimpleRegionStorage;
import net.mayaan.world.level.entity.ChunkEntities;
import net.mayaan.world.level.entity.EntityPersistentStorage;
import net.mayaan.world.level.storage.TagValueInput;
import net.mayaan.world.level.storage.TagValueOutput;
import net.mayaan.world.level.storage.ValueInput;
import org.slf4j.Logger;

public class EntityStorage
implements EntityPersistentStorage<Entity> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String ENTITIES_TAG = "Entities";
    private static final String POSITION_TAG = "Position";
    private final ServerLevel level;
    private final SimpleRegionStorage simpleRegionStorage;
    private final LongSet emptyChunks = new LongOpenHashSet();
    private final ConsecutiveExecutor entityDeserializerQueue;

    public EntityStorage(SimpleRegionStorage simpleRegionStorage, ServerLevel level, Executor mainThreadExecutor) {
        this.simpleRegionStorage = simpleRegionStorage;
        this.level = level;
        this.entityDeserializerQueue = new ConsecutiveExecutor(mainThreadExecutor, "entity-deserializer");
    }

    @Override
    public CompletableFuture<ChunkEntities<Entity>> loadEntities(ChunkPos pos) {
        if (this.emptyChunks.contains(pos.pack())) {
            return CompletableFuture.completedFuture(EntityStorage.emptyChunk(pos));
        }
        CompletableFuture<Optional<CompoundTag>> loadFuture = this.simpleRegionStorage.read(pos);
        this.reportLoadFailureIfPresent(loadFuture, pos);
        return loadFuture.thenApplyAsync(tag -> {
            if (tag.isEmpty()) {
                this.emptyChunks.add(pos.pack());
                return EntityStorage.emptyChunk(pos);
            }
            try {
                ChunkPos storedPos = ((CompoundTag)tag.get()).read(POSITION_TAG, ChunkPos.CODEC).orElseThrow();
                if (!Objects.equals(pos, storedPos)) {
                    LOGGER.error("Chunk file at {} is in the wrong location. (Expected {}, got {})", new Object[]{pos, pos, storedPos});
                    this.level.getServer().reportMisplacedChunk(storedPos, pos, this.simpleRegionStorage.storageInfo());
                }
            }
            catch (Exception e) {
                LOGGER.warn("Failed to parse chunk {} position info", (Object)pos, (Object)e);
                this.level.getServer().reportChunkLoadFailure(e, this.simpleRegionStorage.storageInfo(), pos);
            }
            CompoundTag upgradedChunkTag = this.simpleRegionStorage.upgradeChunkTag((CompoundTag)tag.get(), -1);
            try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(ChunkAccess.problemPath(pos), LOGGER);){
                ValueInput chunkRoot = TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)this.level.registryAccess(), upgradedChunkTag);
                ValueInput.ValueInputList entities = chunkRoot.childrenListOrEmpty(ENTITIES_TAG);
                List<Entity> chunkEntities = EntityType.loadEntitiesRecursive(entities, this.level, EntitySpawnReason.LOAD).toList();
                ChunkEntities<Entity> chunkEntities2 = new ChunkEntities<Entity>(pos, chunkEntities);
                return chunkEntities2;
            }
        }, this.entityDeserializerQueue::schedule);
    }

    private static ChunkEntities<Entity> emptyChunk(ChunkPos pos) {
        return new ChunkEntities<Entity>(pos, List.of());
    }

    @Override
    public void storeEntities(ChunkEntities<Entity> chunk) {
        ChunkPos pos = chunk.getPos();
        if (chunk.isEmpty()) {
            if (this.emptyChunks.add(pos.pack())) {
                this.reportSaveFailureIfPresent(this.simpleRegionStorage.write(pos, IOWorker.STORE_EMPTY), pos);
            }
            return;
        }
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(ChunkAccess.problemPath(pos), LOGGER);){
            ListTag entities = new ListTag();
            chunk.getEntities().forEach(e -> {
                TagValueOutput output = TagValueOutput.createWithContext(reporter.forChild(e.problemPath()), e.registryAccess());
                if (e.save(output)) {
                    CompoundTag result = output.buildResult();
                    entities.add(result);
                }
            });
            CompoundTag chunkTag = NbtUtils.addCurrentDataVersion(new CompoundTag());
            chunkTag.put(ENTITIES_TAG, entities);
            chunkTag.store(POSITION_TAG, ChunkPos.CODEC, pos);
            this.reportSaveFailureIfPresent(this.simpleRegionStorage.write(pos, chunkTag), pos);
            this.emptyChunks.remove(pos.pack());
        }
    }

    private void reportSaveFailureIfPresent(CompletableFuture<?> operation, ChunkPos pos) {
        operation.exceptionally(t -> {
            LOGGER.error("Failed to store entity chunk {}", (Object)pos, t);
            this.level.getServer().reportChunkSaveFailure((Throwable)t, this.simpleRegionStorage.storageInfo(), pos);
            return null;
        });
    }

    private void reportLoadFailureIfPresent(CompletableFuture<?> operation, ChunkPos pos) {
        operation.exceptionally(t -> {
            LOGGER.error("Failed to load entity chunk {}", (Object)pos, t);
            this.level.getServer().reportChunkLoadFailure((Throwable)t, this.simpleRegionStorage.storageInfo(), pos);
            return null;
        });
    }

    @Override
    public void flush(boolean flushStorage) {
        this.simpleRegionStorage.synchronize(flushStorage).join();
        this.entityDeserializerQueue.runAll();
    }

    @Override
    public void close() throws IOException {
        this.simpleRegionStorage.close();
    }
}

