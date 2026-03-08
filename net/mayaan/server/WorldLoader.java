/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 */
package net.mayaan.server;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import net.mayaan.commands.Commands;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.LayeredRegistryAccess;
import net.mayaan.core.Registry;
import net.mayaan.core.RegistryAccess;
import net.mayaan.resources.RegistryDataLoader;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.RegistryLayer;
import net.mayaan.server.ReloadableServerResources;
import net.mayaan.server.packs.PackResources;
import net.mayaan.server.packs.PackType;
import net.mayaan.server.packs.repository.PackRepository;
import net.mayaan.server.packs.resources.CloseableResourceManager;
import net.mayaan.server.packs.resources.MultiPackResourceManager;
import net.mayaan.server.packs.resources.ResourceManager;
import net.mayaan.server.permissions.PermissionSet;
import net.mayaan.tags.TagLoader;
import net.mayaan.world.level.WorldDataConfiguration;

public class WorldLoader {
    public static <D, R> CompletableFuture<R> load(InitConfig config, WorldDataSupplier<D> worldDataSupplier, ResultFactory<D, R> resultFactory, Executor backgroundExecutor, Executor mainThreadExecutor) {
        return CompletableFuture.supplyAsync(config.packConfig::createResourceManager, mainThreadExecutor).thenComposeAsync(packsAndResourceManager -> {
            CloseableResourceManager resources = (CloseableResourceManager)packsAndResourceManager.getSecond();
            LayeredRegistryAccess<RegistryLayer> initialLayers = RegistryLayer.createRegistryAccess();
            List<Registry.PendingTags<?>> staticLayerTags = TagLoader.loadTagsForExistingRegistries(resources, initialLayers.getLayer(RegistryLayer.STATIC));
            RegistryAccess.Frozen worldgenLoadContext = initialLayers.getAccessForLoading(RegistryLayer.WORLDGEN);
            List<HolderLookup.RegistryLookup<?>> worldgenContextRegistries = TagLoader.buildUpdatedLookups(worldgenLoadContext, staticLayerTags);
            return RegistryDataLoader.load(resources, worldgenContextRegistries, RegistryDataLoader.WORLDGEN_REGISTRIES, backgroundExecutor).thenComposeAsync(loadedWorldgenRegistries -> {
                List<HolderLookup.RegistryLookup<?>> dimensionContextRegistries = Stream.concat(worldgenContextRegistries.stream(), loadedWorldgenRegistries.listRegistries()).toList();
                return RegistryDataLoader.load(resources, dimensionContextRegistries, RegistryDataLoader.DIMENSION_REGISTRIES, backgroundExecutor).thenComposeAsync(initialWorldgenDimensions -> {
                    WorldDataConfiguration worldDataConfiguration = (WorldDataConfiguration)packsAndResourceManager.getFirst();
                    HolderLookup.Provider dimensionContextProvider = HolderLookup.Provider.create(dimensionContextRegistries.stream());
                    DataLoadOutput worldDataAndRegistries = worldDataSupplier.get(new DataLoadContext(resources, worldDataConfiguration, dimensionContextProvider, (RegistryAccess.Frozen)initialWorldgenDimensions));
                    LayeredRegistryAccess<RegistryLayer> resourcesLoadContext = initialLayers.replaceFrom(RegistryLayer.WORLDGEN, (RegistryAccess.Frozen)loadedWorldgenRegistries, worldDataAndRegistries.finalDimensions);
                    return ((CompletableFuture)ReloadableServerResources.loadResources(resources, resourcesLoadContext, staticLayerTags, worldDataConfiguration.enabledFeatures(), config.commandSelection(), config.functionCompilationPermissions(), backgroundExecutor, mainThreadExecutor).whenComplete((managers, throwable) -> {
                        if (throwable != null) {
                            resources.close();
                        }
                    })).thenApplyAsync(managers -> {
                        managers.updateComponentsAndStaticRegistryTags();
                        return resultFactory.create(resources, (ReloadableServerResources)managers, resourcesLoadContext, worldDataAndRegistries.cookie);
                    }, mainThreadExecutor);
                }, backgroundExecutor);
            }, backgroundExecutor);
        }, backgroundExecutor);
    }

    public record InitConfig(PackConfig packConfig, Commands.CommandSelection commandSelection, PermissionSet functionCompilationPermissions) {
    }

    public record PackConfig(PackRepository packRepository, WorldDataConfiguration initialDataConfig, boolean safeMode, boolean initMode) {
        public Pair<WorldDataConfiguration, CloseableResourceManager> createResourceManager() {
            WorldDataConfiguration newPackConfig = MayaanServer.configurePackRepository(this.packRepository, this.initialDataConfig, this.initMode, this.safeMode);
            List<PackResources> openedPacks = this.packRepository.openAllSelected();
            MultiPackResourceManager resources = new MultiPackResourceManager(PackType.SERVER_DATA, openedPacks);
            return Pair.of((Object)newPackConfig, (Object)resources);
        }
    }

    @FunctionalInterface
    public static interface WorldDataSupplier<D> {
        public DataLoadOutput<D> get(DataLoadContext var1);
    }

    @FunctionalInterface
    public static interface ResultFactory<D, R> {
        public R create(CloseableResourceManager var1, ReloadableServerResources var2, LayeredRegistryAccess<RegistryLayer> var3, D var4);
    }

    public record DataLoadContext(ResourceManager resources, WorldDataConfiguration dataConfiguration, HolderLookup.Provider datapackWorldgen, RegistryAccess.Frozen datapackDimensions) {
    }

    public record DataLoadOutput<D>(D cookie, RegistryAccess.Frozen finalDimensions) {
    }
}

