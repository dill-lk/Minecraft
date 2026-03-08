/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.levelgen.structure;

import net.mayaan.util.RandomSource;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.StructureManager;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.levelgen.structure.BoundingBox;
import net.mayaan.world.level.levelgen.structure.pieces.PiecesContainer;

@FunctionalInterface
public interface PostPlacementProcessor {
    public static final PostPlacementProcessor NONE = (level, structureManager, generator, random, chunkBB, chunkPos, pieces) -> {};

    public void afterPlace(WorldGenLevel var1, StructureManager var2, ChunkGenerator var3, RandomSource var4, BoundingBox var5, ChunkPos var6, PiecesContainer var7);
}

