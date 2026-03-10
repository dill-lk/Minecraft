/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.levelgen.placement;

import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.chunk.CarvingMask;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.chunk.ProtoChunk;
import net.mayaan.world.level.levelgen.Heightmap;
import net.mayaan.world.level.levelgen.WorldGenerationContext;
import net.mayaan.world.level.levelgen.placement.PlacedFeature;

public class PlacementContext
extends WorldGenerationContext {
    private final WorldGenLevel level;
    private final ChunkGenerator generator;
    private final Optional<PlacedFeature> topFeature;

    public PlacementContext(WorldGenLevel level, ChunkGenerator generator, Optional<PlacedFeature> topFeature) {
        super(generator, level);
        this.level = level;
        this.generator = generator;
        this.topFeature = topFeature;
    }

    public int getHeight(Heightmap.Types type, int x, int z) {
        return this.level.getHeight(type, x, z);
    }

    public CarvingMask getCarvingMask(ChunkPos pos) {
        return ((ProtoChunk)this.level.getChunk(pos.x(), pos.z())).getOrCreateCarvingMask();
    }

    public BlockState getBlockState(BlockPos pos) {
        return this.level.getBlockState(pos);
    }

    public int getMinY() {
        return this.level.getMinY();
    }

    public WorldGenLevel getLevel() {
        return this.level;
    }

    public Optional<PlacedFeature> topFeature() {
        return this.topFeature;
    }

    public ChunkGenerator generator() {
        return this.generator;
    }
}

