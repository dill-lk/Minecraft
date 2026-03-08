/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.Lifecycle
 *  org.slf4j.Logger
 */
package net.mayaan.resources;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import net.mayaan.CrashReport;
import net.mayaan.CrashReportCategory;
import net.mayaan.ReportedException;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.Registry;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.RegistrySynchronization;
import net.mayaan.core.registries.Registries;
import net.mayaan.gametest.framework.GameTestInstance;
import net.mayaan.gametest.framework.TestEnvironmentDefinition;
import net.mayaan.network.chat.ChatType;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.NetworkRegistryLoadTask;
import net.mayaan.resources.RegistryLoadTask;
import net.mayaan.resources.RegistryOps;
import net.mayaan.resources.RegistryValidator;
import net.mayaan.resources.ResourceKey;
import net.mayaan.resources.ResourceManagerRegistryLoadTask;
import net.mayaan.server.dialog.Dialog;
import net.mayaan.server.packs.resources.ResourceManager;
import net.mayaan.server.packs.resources.ResourceProvider;
import net.mayaan.tags.TagNetworkSerialization;
import net.mayaan.world.clock.WorldClock;
import net.mayaan.world.damagesource.DamageType;
import net.mayaan.world.entity.animal.chicken.ChickenSoundVariant;
import net.mayaan.world.entity.animal.chicken.ChickenVariant;
import net.mayaan.world.entity.animal.cow.CowSoundVariant;
import net.mayaan.world.entity.animal.cow.CowVariant;
import net.mayaan.world.entity.animal.feline.CatSoundVariant;
import net.mayaan.world.entity.animal.feline.CatVariant;
import net.mayaan.world.entity.animal.frog.FrogVariant;
import net.mayaan.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.mayaan.world.entity.animal.pig.PigSoundVariant;
import net.mayaan.world.entity.animal.pig.PigVariant;
import net.mayaan.world.entity.animal.wolf.WolfSoundVariant;
import net.mayaan.world.entity.animal.wolf.WolfVariant;
import net.mayaan.world.entity.decoration.painting.PaintingVariant;
import net.mayaan.world.item.Instrument;
import net.mayaan.world.item.JukeboxSong;
import net.mayaan.world.item.enchantment.Enchantment;
import net.mayaan.world.item.enchantment.providers.EnchantmentProvider;
import net.mayaan.world.item.equipment.trim.TrimMaterial;
import net.mayaan.world.item.equipment.trim.TrimPattern;
import net.mayaan.world.item.trading.TradeSet;
import net.mayaan.world.item.trading.VillagerTrade;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.mayaan.world.level.block.entity.BannerPattern;
import net.mayaan.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.mayaan.world.level.dimension.DimensionType;
import net.mayaan.world.level.dimension.LevelStem;
import net.mayaan.world.level.levelgen.DensityFunction;
import net.mayaan.world.level.levelgen.NoiseGeneratorSettings;
import net.mayaan.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.mayaan.world.level.levelgen.feature.ConfiguredFeature;
import net.mayaan.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import net.mayaan.world.level.levelgen.placement.PlacedFeature;
import net.mayaan.world.level.levelgen.presets.WorldPreset;
import net.mayaan.world.level.levelgen.structure.Structure;
import net.mayaan.world.level.levelgen.structure.StructureSet;
import net.mayaan.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.mayaan.world.level.levelgen.synth.NormalNoise;
import net.mayaan.world.timeline.Timeline;
import org.slf4j.Logger;

public class RegistryDataLoader {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Comparator<ResourceKey<?>> ERROR_KEY_COMPARATOR = Comparator.comparing(ResourceKey::registry).thenComparing(ResourceKey::identifier);
    public static final List<RegistryData<?>> WORLDGEN_REGISTRIES = List.of(new RegistryData<DimensionType>(Registries.DIMENSION_TYPE, DimensionType.DIRECT_CODEC), new RegistryData<Biome>(Registries.BIOME, Biome.DIRECT_CODEC), new RegistryData<ChatType>(Registries.CHAT_TYPE, ChatType.DIRECT_CODEC), new RegistryData(Registries.CONFIGURED_CARVER, ConfiguredWorldCarver.DIRECT_CODEC), new RegistryData(Registries.CONFIGURED_FEATURE, ConfiguredFeature.DIRECT_CODEC), new RegistryData<PlacedFeature>(Registries.PLACED_FEATURE, PlacedFeature.DIRECT_CODEC), new RegistryData<Structure>(Registries.STRUCTURE, Structure.DIRECT_CODEC), new RegistryData<StructureSet>(Registries.STRUCTURE_SET, StructureSet.DIRECT_CODEC), new RegistryData<StructureProcessorList>(Registries.PROCESSOR_LIST, StructureProcessorType.DIRECT_CODEC), new RegistryData<StructureTemplatePool>(Registries.TEMPLATE_POOL, StructureTemplatePool.DIRECT_CODEC), new RegistryData<NoiseGeneratorSettings>(Registries.NOISE_SETTINGS, NoiseGeneratorSettings.DIRECT_CODEC), new RegistryData<NormalNoise.NoiseParameters>(Registries.NOISE, NormalNoise.NoiseParameters.DIRECT_CODEC), new RegistryData<DensityFunction>(Registries.DENSITY_FUNCTION, DensityFunction.DIRECT_CODEC), new RegistryData<WorldPreset>(Registries.WORLD_PRESET, WorldPreset.DIRECT_CODEC), new RegistryData<FlatLevelGeneratorPreset>(Registries.FLAT_LEVEL_GENERATOR_PRESET, FlatLevelGeneratorPreset.DIRECT_CODEC), new RegistryData<TrimPattern>(Registries.TRIM_PATTERN, TrimPattern.DIRECT_CODEC), new RegistryData<TrimMaterial>(Registries.TRIM_MATERIAL, TrimMaterial.DIRECT_CODEC), new RegistryData<TrialSpawnerConfig>(Registries.TRIAL_SPAWNER_CONFIG, TrialSpawnerConfig.DIRECT_CODEC), new RegistryData<WolfVariant>(Registries.WOLF_VARIANT, WolfVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<WolfSoundVariant>(Registries.WOLF_SOUND_VARIANT, WolfSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<PigVariant>(Registries.PIG_VARIANT, PigVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<PigSoundVariant>(Registries.PIG_SOUND_VARIANT, PigSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<FrogVariant>(Registries.FROG_VARIANT, FrogVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<CatVariant>(Registries.CAT_VARIANT, CatVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<CatSoundVariant>(Registries.CAT_SOUND_VARIANT, CatSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<CowVariant>(Registries.COW_VARIANT, CowVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<CowSoundVariant>(Registries.COW_SOUND_VARIANT, CowSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<ChickenVariant>(Registries.CHICKEN_VARIANT, ChickenVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<ChickenSoundVariant>(Registries.CHICKEN_SOUND_VARIANT, ChickenSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<ZombieNautilusVariant>(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<PaintingVariant>(Registries.PAINTING_VARIANT, PaintingVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<DamageType>(Registries.DAMAGE_TYPE, DamageType.DIRECT_CODEC), new RegistryData<MultiNoiseBiomeSourceParameterList>(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, MultiNoiseBiomeSourceParameterList.DIRECT_CODEC), new RegistryData<BannerPattern>(Registries.BANNER_PATTERN, BannerPattern.DIRECT_CODEC), new RegistryData<Enchantment>(Registries.ENCHANTMENT, Enchantment.DIRECT_CODEC), new RegistryData<EnchantmentProvider>(Registries.ENCHANTMENT_PROVIDER, EnchantmentProvider.DIRECT_CODEC), new RegistryData<JukeboxSong>(Registries.JUKEBOX_SONG, JukeboxSong.DIRECT_CODEC), new RegistryData<Instrument>(Registries.INSTRUMENT, Instrument.DIRECT_CODEC), new RegistryData(Registries.TEST_ENVIRONMENT, TestEnvironmentDefinition.DIRECT_CODEC), new RegistryData<GameTestInstance>(Registries.TEST_INSTANCE, GameTestInstance.DIRECT_CODEC), new RegistryData<Dialog>(Registries.DIALOG, Dialog.DIRECT_CODEC), new RegistryData<WorldClock>(Registries.WORLD_CLOCK, WorldClock.DIRECT_CODEC), new RegistryData<Timeline>(Registries.TIMELINE, Timeline.DIRECT_CODEC, Timeline::validateRegistry), new RegistryData<VillagerTrade>(Registries.VILLAGER_TRADE, VillagerTrade.CODEC), new RegistryData<TradeSet>(Registries.TRADE_SET, TradeSet.CODEC));
    public static final List<RegistryData<?>> DIMENSION_REGISTRIES = List.of(new RegistryData<LevelStem>(Registries.LEVEL_STEM, LevelStem.CODEC));
    public static final List<RegistryData<?>> SYNCHRONIZED_REGISTRIES = List.of(new RegistryData<Biome>(Registries.BIOME, Biome.NETWORK_CODEC), new RegistryData<ChatType>(Registries.CHAT_TYPE, ChatType.DIRECT_CODEC), new RegistryData<TrimPattern>(Registries.TRIM_PATTERN, TrimPattern.DIRECT_CODEC), new RegistryData<TrimMaterial>(Registries.TRIM_MATERIAL, TrimMaterial.DIRECT_CODEC), new RegistryData<WolfVariant>(Registries.WOLF_VARIANT, WolfVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()), new RegistryData<WolfSoundVariant>(Registries.WOLF_SOUND_VARIANT, WolfSoundVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()), new RegistryData<PigVariant>(Registries.PIG_VARIANT, PigVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()), new RegistryData<PigSoundVariant>(Registries.PIG_SOUND_VARIANT, PigSoundVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()), new RegistryData<FrogVariant>(Registries.FROG_VARIANT, FrogVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()), new RegistryData<CatVariant>(Registries.CAT_VARIANT, CatVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()), new RegistryData<CatSoundVariant>(Registries.CAT_SOUND_VARIANT, CatSoundVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()), new RegistryData<CowSoundVariant>(Registries.COW_SOUND_VARIANT, CowSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<CowVariant>(Registries.COW_VARIANT, CowVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()), new RegistryData<ChickenSoundVariant>(Registries.CHICKEN_SOUND_VARIANT, ChickenSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<ChickenVariant>(Registries.CHICKEN_VARIANT, ChickenVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()), new RegistryData<ZombieNautilusVariant>(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()), new RegistryData<PaintingVariant>(Registries.PAINTING_VARIANT, PaintingVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()), new RegistryData<DimensionType>(Registries.DIMENSION_TYPE, DimensionType.NETWORK_CODEC), new RegistryData<DamageType>(Registries.DAMAGE_TYPE, DamageType.DIRECT_CODEC), new RegistryData<BannerPattern>(Registries.BANNER_PATTERN, BannerPattern.DIRECT_CODEC), new RegistryData<Enchantment>(Registries.ENCHANTMENT, Enchantment.DIRECT_CODEC), new RegistryData<JukeboxSong>(Registries.JUKEBOX_SONG, JukeboxSong.DIRECT_CODEC), new RegistryData<Instrument>(Registries.INSTRUMENT, Instrument.DIRECT_CODEC), new RegistryData(Registries.TEST_ENVIRONMENT, TestEnvironmentDefinition.DIRECT_CODEC), new RegistryData<GameTestInstance>(Registries.TEST_INSTANCE, GameTestInstance.DIRECT_CODEC), new RegistryData<Dialog>(Registries.DIALOG, Dialog.DIRECT_CODEC), new RegistryData<WorldClock>(Registries.WORLD_CLOCK, WorldClock.DIRECT_CODEC), new RegistryData<Timeline>(Registries.TIMELINE, Timeline.NETWORK_CODEC));

    public static CompletableFuture<RegistryAccess.Frozen> load(final ResourceManager resourceManager, List<HolderLookup.RegistryLookup<?>> contextRegistries, List<RegistryData<?>> registriesToLoad, Executor executor) {
        LoaderFactory loaderFactory = new LoaderFactory(){

            @Override
            public <T> RegistryLoadTask<T> create(RegistryData<T> data, Map<ResourceKey<?>, Exception> loadingErrors) {
                return new ResourceManagerRegistryLoadTask<T>(data, Lifecycle.stable(), loadingErrors, resourceManager);
            }
        };
        return RegistryDataLoader.load(loaderFactory, contextRegistries, registriesToLoad, executor);
    }

    public static CompletableFuture<RegistryAccess.Frozen> load(final Map<ResourceKey<? extends Registry<?>>, NetworkedRegistryData> entries, final ResourceProvider knownDataSource, List<HolderLookup.RegistryLookup<?>> contextRegistries, List<RegistryData<?>> registriesToLoad, Executor executor) {
        LoaderFactory loaderFactory = new LoaderFactory(){

            @Override
            public <T> RegistryLoadTask<T> create(RegistryData<T> data, Map<ResourceKey<?>, Exception> loadingErrors) {
                return new NetworkRegistryLoadTask<T>(data, Lifecycle.stable(), loadingErrors, entries, knownDataSource);
            }
        };
        return RegistryDataLoader.load(loaderFactory, contextRegistries, registriesToLoad, executor);
    }

    private static CompletableFuture<RegistryAccess.Frozen> load(LoaderFactory loaderFactory, List<HolderLookup.RegistryLookup<?>> contextRegistries, List<RegistryData<?>> registriesToLoad, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            ConcurrentHashMap loadingErrors = new ConcurrentHashMap();
            List<RegistryLoadTask<?>> loadTasks = registriesToLoad.stream().map(r -> loaderFactory.create(r, loadingErrors)).collect(Collectors.toUnmodifiableList());
            RegistryOps.RegistryInfoLookup contextAndNewRegistries = RegistryDataLoader.createContext(contextRegistries, loadTasks);
            int taskCount = loadTasks.size();
            CompletableFuture[] loadCompletions = new CompletableFuture[taskCount];
            for (int i = 0; i < taskCount; ++i) {
                loadCompletions[i] = loadTasks.get(i).load(contextAndNewRegistries, executor);
            }
            return CompletableFuture.allOf(loadCompletions).thenApplyAsync(ignored -> {
                List<RegistryLoadTask> frozenRegistries = loadTasks.stream().filter(task -> task.freezeRegistry(loadingErrors)).toList();
                if (!loadingErrors.isEmpty()) {
                    throw RegistryDataLoader.logErrors(loadingErrors);
                }
                List registries = frozenRegistries.stream().flatMap(task -> task.validateRegistry(loadingErrors).stream()).toList();
                if (!loadingErrors.isEmpty()) {
                    throw RegistryDataLoader.logErrors(loadingErrors);
                }
                return new RegistryAccess.ImmutableRegistryAccess(registries).freeze();
            }, executor);
        }, executor).thenCompose(c -> c);
    }

    private static RegistryOps.RegistryInfoLookup createContext(List<HolderLookup.RegistryLookup<?>> contextRegistries, List<RegistryLoadTask<?>> newRegistriesAndLoaders) {
        final HashMap result = new HashMap();
        contextRegistries.forEach(e -> result.put(e.key(), RegistryDataLoader.createInfoForContextRegistry(e)));
        newRegistriesAndLoaders.forEach(e -> result.put(e.registryKey(), e.createRegistryInfo()));
        return new RegistryOps.RegistryInfoLookup(){

            @Override
            public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
                return Optional.ofNullable((RegistryOps.RegistryInfo)result.get(key));
            }
        };
    }

    private static <T> RegistryOps.RegistryInfo<T> createInfoForContextRegistry(HolderLookup.RegistryLookup<T> lookup) {
        return new RegistryOps.RegistryInfo<T>(lookup, lookup, lookup.registryLifecycle());
    }

    private static ReportedException logErrors(Map<ResourceKey<?>, Exception> loadingErrors) {
        RegistryDataLoader.printFullDetailsToLog(loadingErrors);
        return RegistryDataLoader.createReportWithBriefInfo(loadingErrors);
    }

    private static void printFullDetailsToLog(Map<ResourceKey<?>, Exception> loadingErrors) {
        StringWriter collectedErrors = new StringWriter();
        PrintWriter errorPrinter = new PrintWriter(collectedErrors);
        Map<Identifier, Map<Identifier, Exception>> errorsByRegistry = loadingErrors.entrySet().stream().collect(Collectors.groupingBy(e -> ((ResourceKey)e.getKey()).registry(), Collectors.toMap(e -> ((ResourceKey)e.getKey()).identifier(), Map.Entry::getValue)));
        errorsByRegistry.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(registryEntry -> {
            errorPrinter.printf(Locale.ROOT, "> Errors in registry %s:%n", registryEntry.getKey());
            ((Map)registryEntry.getValue()).entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(elementError -> {
                errorPrinter.printf(Locale.ROOT, ">> Errors in element %s:%n", elementError.getKey());
                ((Exception)elementError.getValue()).printStackTrace(errorPrinter);
            });
        });
        errorPrinter.flush();
        LOGGER.error("Registry loading errors:\n{}", (Object)collectedErrors);
    }

    private static ReportedException createReportWithBriefInfo(Map<ResourceKey<?>, Exception> loadingErrors) {
        CrashReport report = CrashReport.forThrowable(new IllegalStateException("Failed to load registries due to errors"), "Registry Loading");
        CrashReportCategory errors = report.addCategory("Loading info");
        errors.setDetail("Errors", () -> {
            StringBuilder briefDetails = new StringBuilder();
            loadingErrors.entrySet().stream().sorted(Map.Entry.comparingByKey(ERROR_KEY_COMPARATOR)).forEach(e -> briefDetails.append("\n\t\t").append(((ResourceKey)e.getKey()).registry()).append("/").append(((ResourceKey)e.getKey()).identifier()).append(": ").append(((Exception)e.getValue()).getMessage()));
            return briefDetails.toString();
        });
        return new ReportedException(report);
    }

    @FunctionalInterface
    private static interface LoaderFactory {
        public <T> RegistryLoadTask<T> create(RegistryData<T> var1, Map<ResourceKey<?>, Exception> var2);
    }

    public record RegistryData<T>(ResourceKey<? extends Registry<T>> key, Codec<T> elementCodec, RegistryValidator<T> validator) {
        private RegistryData(ResourceKey<? extends Registry<T>> key, Codec<T> elementCodec) {
            this(key, elementCodec, RegistryValidator.none());
        }

        public void runWithArguments(BiConsumer<ResourceKey<? extends Registry<T>>, Codec<T>> output) {
            output.accept(this.key, this.elementCodec);
        }
    }

    public record NetworkedRegistryData(List<RegistrySynchronization.PackedRegistryEntry> elements, TagNetworkSerialization.NetworkPayload tags) {
    }
}

