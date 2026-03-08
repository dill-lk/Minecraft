/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.level;

import java.util.concurrent.CompletableFuture;
import net.mayaan.server.level.ChunkGenerationTask;
import net.mayaan.server.level.GenerationChunkHolder;
import net.mayaan.util.StaticCache2D;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.chunk.ChunkAccess;
import net.mayaan.world.level.chunk.status.ChunkStatus;
import net.mayaan.world.level.chunk.status.ChunkStep;

public interface GeneratingChunkMap {
    public GenerationChunkHolder acquireGeneration(long var1);

    public void releaseGeneration(GenerationChunkHolder var1);

    public CompletableFuture<ChunkAccess> applyStep(GenerationChunkHolder var1, ChunkStep var2, StaticCache2D<GenerationChunkHolder> var3);

    public ChunkGenerationTask scheduleGenerationTask(ChunkStatus var1, ChunkPos var2);

    public void runGenerationTasks();
}

