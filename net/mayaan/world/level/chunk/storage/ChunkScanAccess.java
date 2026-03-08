/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.chunk.storage;

import java.util.concurrent.CompletableFuture;
import net.mayaan.nbt.StreamTagVisitor;
import net.mayaan.world.level.ChunkPos;

public interface ChunkScanAccess {
    public CompletableFuture<Void> scanChunk(ChunkPos var1, StreamTagVisitor var2);
}

