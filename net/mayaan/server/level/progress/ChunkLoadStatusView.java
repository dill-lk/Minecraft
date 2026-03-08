/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.server.level.progress;

import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.status.ChunkStatus;
import org.jspecify.annotations.Nullable;

public interface ChunkLoadStatusView {
    public void moveTo(ResourceKey<Level> var1, ChunkPos var2);

    public @Nullable ChunkStatus get(int var1, int var2);

    public int radius();
}

