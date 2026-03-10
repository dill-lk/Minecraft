/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.worldgen.biome;

import net.mayaan.core.HolderGetter;
import net.mayaan.data.worldgen.BiomeDefaultFeatures;
import net.mayaan.data.worldgen.placement.EndPlacements;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.biome.BiomeGenerationSettings;
import net.mayaan.world.level.biome.BiomeSpecialEffects;
import net.mayaan.world.level.biome.MobSpawnSettings;
import net.mayaan.world.level.levelgen.GenerationStep;
import net.mayaan.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.mayaan.world.level.levelgen.placement.PlacedFeature;

public class EndBiomes {
    private static Biome baseEndBiome(BiomeGenerationSettings.Builder generation) {
        MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.endSpawns(mobs);
        return new Biome.BiomeBuilder().hasPrecipitation(false).temperature(0.5f).downfall(0.5f).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).build()).mobSpawnSettings(mobs.build()).generationSettings(generation.build()).build();
    }

    public static Biome endBarrens(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
        return EndBiomes.baseEndBiome(generation);
    }

    public static Biome theEnd(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EndPlacements.END_SPIKE).addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, EndPlacements.END_PLATFORM);
        return EndBiomes.baseEndBiome(generation);
    }

    public static Biome endMidlands(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
        return EndBiomes.baseEndBiome(generation);
    }

    public static Biome endHighlands(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EndPlacements.CHORUS_PLANT);
        return EndBiomes.baseEndBiome(generation);
    }

    public static Biome smallEndIslands(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers).addFeature(GenerationStep.Decoration.RAW_GENERATION, EndPlacements.END_ISLAND_DECORATED);
        return EndBiomes.baseEndBiome(generation);
    }
}

