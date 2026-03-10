/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.levelgen.structure.pieces;

import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.LevelHeightAccessor;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.levelgen.WorldgenRandom;
import net.mayaan.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.mayaan.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

@FunctionalInterface
public interface PieceGenerator<C extends FeatureConfiguration> {
    public void generatePieces(StructurePiecesBuilder var1, Context<C> var2);

    public record Context<C extends FeatureConfiguration>(C config, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, WorldgenRandom random, long seed) {
    }
}

