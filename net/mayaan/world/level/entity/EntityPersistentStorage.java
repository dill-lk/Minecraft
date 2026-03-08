/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.entity;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.entity.ChunkEntities;

public interface EntityPersistentStorage<T>
extends AutoCloseable {
    public CompletableFuture<ChunkEntities<T>> loadEntities(ChunkPos var1);

    public void storeEntities(ChunkEntities<T> var1);

    public void flush(boolean var1);

    @Override
    default public void close() throws IOException {
    }
}

