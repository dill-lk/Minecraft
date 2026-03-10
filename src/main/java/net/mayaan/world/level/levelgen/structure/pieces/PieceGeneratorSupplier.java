/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.levelgen.structure.pieces;

import java.util.Optional;
import java.util.function.Predicate;
import net.mayaan.core.Holder;
import net.mayaan.core.QuartPos;
import net.mayaan.core.RegistryAccess;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.LevelHeightAccessor;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.biome.BiomeSource;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.levelgen.Heightmap;
import net.mayaan.world.level.levelgen.RandomState;
import net.mayaan.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.mayaan.world.level.levelgen.structure.pieces.PieceGenerator;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

@FunctionalInterface
public interface PieceGeneratorSupplier<C extends FeatureConfiguration> {
    public Optional<PieceGenerator<C>> createGenerator(Context<C> var1);

    public static <C extends FeatureConfiguration> PieceGeneratorSupplier<C> simple(Predicate<Context<C>> check, PieceGenerator<C> generator) {
        Optional result = Optional.of(generator);
        return context -> check.test(context) ? result : Optional.empty();
    }

    public static <C extends FeatureConfiguration> Predicate<Context<C>> checkForBiomeOnTop(Heightmap.Types type) {
        return context -> context.validBiomeOnTop(type);
    }

    public record Context<C extends FeatureConfiguration>(ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, long seed, ChunkPos chunkPos, C config, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome, StructureTemplateManager structureTemplateManager, RegistryAccess registryAccess) {
        public boolean validBiomeOnTop(Heightmap.Types type) {
            int blockX = this.chunkPos.getMiddleBlockX();
            int blockZ = this.chunkPos.getMiddleBlockZ();
            int blockY = this.chunkGenerator.getFirstOccupiedHeight(blockX, blockZ, type, this.heightAccessor, this.randomState);
            Holder<Biome> biome = this.chunkGenerator.getBiomeSource().getNoiseBiome(QuartPos.fromBlock(blockX), QuartPos.fromBlock(blockY), QuartPos.fromBlock(blockZ), this.randomState.sampler());
            return this.validBiome.test(biome);
        }
    }
}

