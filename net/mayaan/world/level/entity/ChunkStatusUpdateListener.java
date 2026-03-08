/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.entity;

import net.mayaan.server.level.FullChunkStatus;
import net.mayaan.world.level.ChunkPos;

@FunctionalInterface
public interface ChunkStatusUpdateListener {
    public void onChunkStatusChange(ChunkPos var1, FullChunkStatus var2);
}

