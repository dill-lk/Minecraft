/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.worldselection;

import java.util.Map;
import java.util.Optional;
import net.mayaan.client.gui.screens.CreateBuffetWorldScreen;
import net.mayaan.client.gui.screens.CreateFlatWorldScreen;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.worldselection.CreateWorldScreen;
import net.mayaan.client.gui.screens.worldselection.WorldCreationContext;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.biome.BiomeSource;
import net.mayaan.world.level.biome.FixedBiomeSource;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.levelgen.FlatLevelSource;
import net.mayaan.world.level.levelgen.NoiseBasedChunkGenerator;
import net.mayaan.world.level.levelgen.NoiseGeneratorSettings;
import net.mayaan.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.mayaan.world.level.levelgen.presets.WorldPreset;
import net.mayaan.world.level.levelgen.presets.WorldPresets;

public interface PresetEditor {
    public static final Map<Optional<ResourceKey<WorldPreset>>, PresetEditor> EDITORS = Map.of(Optional.of(WorldPresets.FLAT), (parent, settings) -> {
        ChunkGenerator overworld = settings.selectedDimensions().overworld();
        RegistryAccess.Frozen registryAccess = settings.worldgenLoadContext();
        HolderLookup.RegistryLookup biomes = registryAccess.lookupOrThrow(Registries.BIOME);
        HolderLookup.RegistryLookup structureSets = registryAccess.lookupOrThrow(Registries.STRUCTURE_SET);
        HolderLookup.RegistryLookup placedFeatures = registryAccess.lookupOrThrow(Registries.PLACED_FEATURE);
        return new CreateFlatWorldScreen(parent, flatWorldSettings -> parent.getUiState().updateDimensions(PresetEditor.flatWorldConfigurator(flatWorldSettings)), overworld instanceof FlatLevelSource ? ((FlatLevelSource)overworld).settings() : FlatLevelGeneratorSettings.getDefault(biomes, structureSets, placedFeatures));
    }, Optional.of(WorldPresets.SINGLE_BIOME_SURFACE), (parent, settings) -> new CreateBuffetWorldScreen(parent, settings, biome -> parent.getUiState().updateDimensions(PresetEditor.fixedBiomeConfigurator(biome))));

    public Screen createEditScreen(CreateWorldScreen var1, WorldCreationContext var2);

    public static WorldCreationContext.DimensionsUpdater flatWorldConfigurator(FlatLevelGeneratorSettings generatorSettings) {
        return (registryAccess, dimensions) -> {
            FlatLevelSource generator = new FlatLevelSource(generatorSettings);
            return dimensions.replaceOverworldGenerator((HolderLookup.Provider)registryAccess, generator);
        };
    }

    private static WorldCreationContext.DimensionsUpdater fixedBiomeConfigurator(Holder<Biome> biome) {
        return (registryAccess, dimensions) -> {
            HolderLookup.RegistryLookup noiseGeneratorSettings = registryAccess.lookupOrThrow(Registries.NOISE_SETTINGS);
            Holder.Reference noiseSettings = noiseGeneratorSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);
            FixedBiomeSource biomeSource = new FixedBiomeSource(biome);
            NoiseBasedChunkGenerator generator = new NoiseBasedChunkGenerator((BiomeSource)biomeSource, noiseSettings);
            return dimensions.replaceOverworldGenerator((HolderLookup.Provider)registryAccess, generator);
        };
    }
}

