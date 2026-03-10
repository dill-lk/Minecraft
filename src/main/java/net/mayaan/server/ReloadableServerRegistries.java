/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.mojang.logging.LogUtils
 *  com.mojang.serialization.DynamicOps
 *  com.mojang.serialization.JsonOps
 *  com.mojang.serialization.Lifecycle
 *  org.slf4j.Logger
 */
package net.mayaan.server;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.LayeredRegistryAccess;
import net.mayaan.core.MappedRegistry;
import net.mayaan.core.RegistrationInfo;
import net.mayaan.core.Registry;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.WritableRegistry;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.RegistryOps;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.RegistryLayer;
import net.mayaan.server.packs.resources.ResourceManager;
import net.mayaan.server.packs.resources.SimpleJsonResourceReloadListener;
import net.mayaan.tags.TagLoader;
import net.mayaan.util.ProblemReporter;
import net.mayaan.util.Util;
import net.mayaan.world.level.storage.loot.LootDataType;
import net.mayaan.world.level.storage.loot.LootTable;
import net.mayaan.world.level.storage.loot.Validatable;
import net.mayaan.world.level.storage.loot.ValidationContextSource;
import org.slf4j.Logger;

public class ReloadableServerRegistries {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final RegistrationInfo DEFAULT_REGISTRATION_INFO = new RegistrationInfo(Optional.empty(), Lifecycle.experimental());

    public static CompletableFuture<LoadResult> reload(LayeredRegistryAccess<RegistryLayer> context, List<Registry.PendingTags<?>> updatedContextTags, ResourceManager manager, Executor executor) {
        List<HolderLookup.RegistryLookup<?>> contextRegistriesWithTags = TagLoader.buildUpdatedLookups(context.getAccessForLoading(RegistryLayer.RELOADABLE), updatedContextTags);
        HolderLookup.Provider loadingContextWithTags = HolderLookup.Provider.create(contextRegistriesWithTags.stream());
        RegistryOps ops = loadingContextWithTags.createSerializationContext(JsonOps.INSTANCE);
        List<CompletableFuture> registryLoads = LootDataType.values().map(type -> ReloadableServerRegistries.scheduleRegistryLoad(type, ops, manager, executor)).toList();
        CompletableFuture sequence = Util.sequence(registryLoads);
        return sequence.thenApplyAsync(newlyLoadedRegistries -> ReloadableServerRegistries.createAndValidateFullContext(context, loadingContextWithTags, newlyLoadedRegistries), executor);
    }

    private static <T extends Validatable> CompletableFuture<WritableRegistry<?>> scheduleRegistryLoad(LootDataType<T> type, RegistryOps<JsonElement> ops, ResourceManager manager, Executor taskExecutor) {
        return CompletableFuture.supplyAsync(() -> {
            MappedRegistry registry = new MappedRegistry(type.registryKey(), Lifecycle.experimental());
            HashMap<Identifier, Validatable> elements = new HashMap<Identifier, Validatable>();
            SimpleJsonResourceReloadListener.scanDirectory(manager, type.registryKey(), (DynamicOps<JsonElement>)ops, type.codec(), elements);
            elements.forEach((id, element) -> registry.register(ResourceKey.create(type.registryKey(), id), element, DEFAULT_REGISTRATION_INFO));
            TagLoader.loadTagsForRegistry(manager, registry);
            return registry;
        }, taskExecutor);
    }

    private static LoadResult createAndValidateFullContext(LayeredRegistryAccess<RegistryLayer> contextLayers, HolderLookup.Provider contextLookupWithUpdatedTags, List<WritableRegistry<?>> newRegistries) {
        LayeredRegistryAccess<RegistryLayer> fullLayers = ReloadableServerRegistries.createUpdatedRegistries(contextLayers, newRegistries);
        HolderLookup.Provider fullLookupWithUpdatedTags = ReloadableServerRegistries.concatenateLookups(contextLookupWithUpdatedTags, fullLayers.getLayer(RegistryLayer.RELOADABLE));
        ReloadableServerRegistries.validateLootRegistries(fullLookupWithUpdatedTags);
        return new LoadResult(fullLayers, fullLookupWithUpdatedTags);
    }

    private static HolderLookup.Provider concatenateLookups(HolderLookup.Provider first, HolderLookup.Provider second) {
        return HolderLookup.Provider.create(Stream.concat(first.listRegistries(), second.listRegistries()));
    }

    private static void validateLootRegistries(HolderLookup.Provider fullContextWithNewTags) {
        ProblemReporter.Collector problems = new ProblemReporter.Collector();
        ValidationContextSource contextSource = new ValidationContextSource(problems, fullContextWithNewTags);
        LootDataType.values().forEach(lootDataType -> ReloadableServerRegistries.validateRegistry(contextSource, lootDataType, fullContextWithNewTags));
        problems.forEach((id, problem) -> LOGGER.warn("Found loot table element validation problem in {}: {}", id, (Object)problem.description()));
    }

    private static LayeredRegistryAccess<RegistryLayer> createUpdatedRegistries(LayeredRegistryAccess<RegistryLayer> context, List<WritableRegistry<?>> registries) {
        return context.replaceFrom(RegistryLayer.RELOADABLE, new RegistryAccess.ImmutableRegistryAccess(registries).freeze());
    }

    private static <T extends Validatable> void validateRegistry(ValidationContextSource contextSource, LootDataType<T> type, HolderLookup.Provider registries) {
        HolderGetter registry = registries.lookupOrThrow(type.registryKey());
        type.runValidation(contextSource, (HolderLookup<T>)registry);
    }

    public record LoadResult(LayeredRegistryAccess<RegistryLayer> layers, HolderLookup.Provider lookupWithUpdatedTags) {
    }

    public static class Holder {
        private final HolderLookup.Provider registries;

        public Holder(HolderLookup.Provider registries) {
            this.registries = registries;
        }

        public HolderLookup.Provider lookup() {
            return this.registries;
        }

        public LootTable getLootTable(ResourceKey<LootTable> id) {
            return this.registries.lookup(Registries.LOOT_TABLE).flatMap(r -> r.get(id)).map(net.mayaan.core.Holder::value).orElse(LootTable.EMPTY);
        }
    }
}

