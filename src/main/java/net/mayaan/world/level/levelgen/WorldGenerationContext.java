/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.levelgen;

import net.mayaan.world.level.LevelHeightAccessor;
import net.mayaan.world.level.chunk.ChunkGenerator;

public class WorldGenerationContext {
    private final int minY;
    private final int height;

    public WorldGenerationContext(ChunkGenerator generator, LevelHeightAccessor heightAccessor) {
        this.minY = Math.max(heightAccessor.getMinY(), generator.getMinY());
        this.height = Math.min(heightAccessor.getHeight(), generator.getGenDepth());
    }

    public int getMinGenY() {
        return this.minY;
    }

    public int getGenDepth() {
        return this.height;
    }
}

