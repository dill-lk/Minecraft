/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.worldselection;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import net.mayaan.client.gui.screens.worldselection.InitialWorldCreationOptions;
import net.mayaan.client.gui.screens.worldselection.WorldCreationUiState;
import net.mayaan.core.LayeredRegistryAccess;
import net.mayaan.core.Registry;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.registries.Registries;
import net.mayaan.server.RegistryLayer;
import net.mayaan.server.ReloadableServerResources;
import net.mayaan.world.level.WorldDataConfiguration;
import net.mayaan.world.level.dimension.LevelStem;
import net.mayaan.world.level.gamerules.GameRuleMap;
import net.mayaan.world.level.levelgen.WorldDimensions;
import net.mayaan.world.level.levelgen.WorldGenSettings;
import net.mayaan.world.level.levelgen.WorldOptions;

public record WorldCreationContext(WorldOptions options, Registry<LevelStem> datapackDimensions, WorldDimensions selectedDimensions, LayeredRegistryAccess<RegistryLayer> worldgenRegistries, ReloadableServerResources dataPackResources, WorldDataConfiguration dataConfiguration, InitialWorldCreationOptions initialWorldCreationOptions) {
    public WorldCreationContext(WorldGenSettings worldGenSettings, LayeredRegistryAccess<RegistryLayer> loadedRegistries, ReloadableServerResources dataPackResources, WorldDataConfiguration dataConfiguration) {
        this(worldGenSettings.options(), worldGenSettings.dimensions(), loadedRegistries, dataPackResources, dataConfiguration, new InitialWorldCreationOptions(WorldCreationUiState.SelectedGameMode.SURVIVAL, GameRuleMap.of(), null));
    }

    public WorldCreationContext(WorldOptions worldOptions, WorldDimensions worldDimensions, LayeredRegistryAccess<RegistryLayer> loadedRegistries, ReloadableServerResources dataPackResources, WorldDataConfiguration dataConfiguration, InitialWorldCreationOptions initialWorldCreationOptions) {
        this(worldOptions, (Registry<LevelStem>)loadedRegistries.getLayer(RegistryLayer.DIMENSIONS).lookupOrThrow(Registries.LEVEL_STEM), worldDimensions, loadedRegistries.replaceFrom(RegistryLayer.DIMENSIONS, new RegistryAccess.Frozen[0]), dataPackResources, dataConfiguration, initialWorldCreationOptions);
    }

    public WorldCreationContext withSettings(WorldOptions options, WorldDimensions dimensions) {
        return new WorldCreationContext(options, this.datapackDimensions, dimensions, this.worldgenRegistries, this.dataPackResources, this.dataConfiguration, this.initialWorldCreationOptions);
    }

    public WorldCreationContext withOptions(OptionsModifier modifier) {
        return new WorldCreationContext((WorldOptions)modifier.apply(this.options), this.datapackDimensions, this.selectedDimensions, this.worldgenRegistries, this.dataPackResources, this.dataConfiguration, this.initialWorldCreationOptions);
    }

    public WorldCreationContext withDimensions(DimensionsUpdater modifier) {
        return new WorldCreationContext(this.options, this.datapackDimensions, (WorldDimensions)modifier.apply(this.worldgenLoadContext(), this.selectedDimensions), this.worldgenRegistries, this.dataPackResources, this.dataConfiguration, this.initialWorldCreationOptions);
    }

    public RegistryAccess.Frozen worldgenLoadContext() {
        return this.worldgenRegistries.compositeAccess();
    }

    public void validate() {
        for (LevelStem stem : this.datapackDimensions()) {
            stem.generator().validate();
        }
    }

    public static interface OptionsModifier
    extends UnaryOperator<WorldOptions> {
    }

    @FunctionalInterface
    public static interface DimensionsUpdater
    extends BiFunction<RegistryAccess.Frozen, WorldDimensions, WorldDimensions> {
    }
}

