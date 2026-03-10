/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.worldupdate;

import java.util.List;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.chunk.storage.RegionFile;

public record FileToUpgrade(RegionFile file, List<ChunkPos> chunksToUpgrade) {
}

