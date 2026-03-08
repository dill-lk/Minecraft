/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.chunk.status;

import java.util.concurrent.CompletableFuture;
import net.mayaan.server.level.GenerationChunkHolder;
import net.mayaan.util.StaticCache2D;
import net.mayaan.world.level.chunk.ChunkAccess;
import net.mayaan.world.level.chunk.status.ChunkStep;
import net.mayaan.world.level.chunk.status.WorldGenContext;

@FunctionalInterface
public interface ChunkStatusTask {
    public CompletableFuture<ChunkAccess> doWork(WorldGenContext var1, ChunkStep var2, StaticCache2D<GenerationChunkHolder> var3, ChunkAccess var4);
}

