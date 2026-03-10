/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.registries;

import java.util.List;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.HolderSet;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.RegistrySetBuilder;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.Carvers;
import net.mayaan.data.worldgen.DimensionTypes;
import net.mayaan.data.worldgen.NoiseData;
import net.mayaan.data.worldgen.Pools;
import net.mayaan.data.worldgen.ProcessorLists;
import net.mayaan.data.worldgen.StructureSets;
import net.mayaan.data.worldgen.Structures;
import net.mayaan.data.worldgen.biome.BiomeData;
import net.mayaan.data.worldgen.features.FeatureUtils;
import net.mayaan.data.worldgen.placement.PlacementUtils;
import net.mayaan.gametest.framework.GameTestEnvironments;
import net.mayaan.gametest.framework.GameTestInstances;
import net.mayaan.network.chat.ChatType;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.dialog.Dialogs;
import net.mayaan.util.Util;
import net.mayaan.world.clock.WorldClocks;
import net.mayaan.world.damagesource.DamageTypes;
import net.mayaan.world.entity.animal.chicken.ChickenSoundVariants;
import net.mayaan.world.entity.animal.chicken.ChickenVariants;
import net.mayaan.world.entity.animal.cow.CowSoundVariants;
import net.mayaan.world.entity.animal.cow.CowVariants;
import net.mayaan.world.entity.animal.feline.CatSoundVariants;
import net.mayaan.world.entity.animal.feline.CatVariants;
import net.mayaan.world.entity.animal.frog.FrogVariants;
import net.mayaan.world.entity.animal.nautilus.ZombieNautilusVariants;
import net.mayaan.world.entity.animal.pig.PigSoundVariants;
import net.mayaan.world.entity.animal.pig.PigVariants;
import net.mayaan.world.entity.animal.wolf.WolfSoundVariants;
import net.mayaan.world.entity.animal.wolf.WolfVariants;
import net.mayaan.world.entity.decoration.painting.PaintingVariants;
import net.mayaan.world.item.Instruments;
import net.mayaan.world.item.JukeboxSongs;
import net.mayaan.world.item.enchantment.Enchantments;
import net.mayaan.world.item.enchantment.providers.VanillaEnchantmentProviders;
import net.mayaan.world.item.equipment.trim.TrimMaterials;
import net.mayaan.world.item.equipment.trim.TrimPatterns;
import net.mayaan.world.item.trading.TradeSets;
import net.mayaan.world.item.trading.VillagerTrades;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.mayaan.world.level.block.entity.BannerPatterns;
import net.mayaan.world.level.block.entity.trialspawner.TrialSpawnerConfigs;
import net.mayaan.world.level.levelgen.NoiseGeneratorSettings;
import net.mayaan.world.level.levelgen.NoiseRouterData;
import net.mayaan.world.level.levelgen.flat.FlatLevelGeneratorPresets;
import net.mayaan.world.level.levelgen.placement.BiomeFilter;
import net.mayaan.world.level.levelgen.placement.PlacedFeature;
import net.mayaan.world.level.levelgen.presets.WorldPresets;
import net.mayaan.world.timeline.Timelines;

public class VanillaRegistries {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.DIMENSION_TYPE, DimensionTypes::bootstrap).add(Registries.CONFIGURED_CARVER, Carvers::bootstrap).add(Registries.CONFIGURED_FEATURE, FeatureUtils::bootstrap).add(Registries.PLACED_FEATURE, PlacementUtils::bootstrap).add(Registries.STRUCTURE, Structures::bootstrap).add(Registries.STRUCTURE_SET, StructureSets::bootstrap).add(Registries.PROCESSOR_LIST, ProcessorLists::bootstrap).add(Registries.TEMPLATE_POOL, Pools::bootstrap).add(Registries.BIOME, BiomeData::bootstrap).add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, MultiNoiseBiomeSourceParameterLists::bootstrap).add(Registries.NOISE, NoiseData::bootstrap).add(Registries.DENSITY_FUNCTION, NoiseRouterData::bootstrap).add(Registries.NOISE_SETTINGS, NoiseGeneratorSettings::bootstrap).add(Registries.WORLD_PRESET, WorldPresets::bootstrap).add(Registries.FLAT_LEVEL_GENERATOR_PRESET, FlatLevelGeneratorPresets::bootstrap).add(Registries.CHAT_TYPE, ChatType::bootstrap).add(Registries.TRIM_PATTERN, TrimPatterns::bootstrap).add(Registries.TRIM_MATERIAL, TrimMaterials::bootstrap).add(Registries.TRIAL_SPAWNER_CONFIG, TrialSpawnerConfigs::bootstrap).add(Registries.WOLF_VARIANT, WolfVariants::bootstrap).add(Registries.WOLF_SOUND_VARIANT, WolfSoundVariants::bootstrap).add(Registries.PAINTING_VARIANT, PaintingVariants::bootstrap).add(Registries.DAMAGE_TYPE, DamageTypes::bootstrap).add(Registries.BANNER_PATTERN, BannerPatterns::bootstrap).add(Registries.ENCHANTMENT, Enchantments::bootstrap).add(Registries.ENCHANTMENT_PROVIDER, VanillaEnchantmentProviders::bootstrap).add(Registries.JUKEBOX_SONG, JukeboxSongs::bootstrap).add(Registries.INSTRUMENT, Instruments::bootstrap).add(Registries.PIG_VARIANT, PigVariants::bootstrap).add(Registries.PIG_SOUND_VARIANT, PigSoundVariants::bootstrap).add(Registries.COW_VARIANT, CowVariants::bootstrap).add(Registries.COW_SOUND_VARIANT, CowSoundVariants::bootstrap).add(Registries.CHICKEN_VARIANT, ChickenVariants::bootstrap).add(Registries.CHICKEN_SOUND_VARIANT, ChickenSoundVariants::bootstrap).add(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariants::bootstrap).add(Registries.TEST_ENVIRONMENT, GameTestEnvironments::bootstrap).add(Registries.TEST_INSTANCE, GameTestInstances::bootstrap).add(Registries.FROG_VARIANT, FrogVariants::bootstrap).add(Registries.CAT_VARIANT, CatVariants::bootstrap).add(Registries.CAT_SOUND_VARIANT, CatSoundVariants::bootstrap).add(Registries.DIALOG, Dialogs::bootstrap).add(Registries.WORLD_CLOCK, WorldClocks::bootstrap).add(Registries.TIMELINE, Timelines::bootstrap).add(Registries.VILLAGER_TRADE, VillagerTrades::bootstrap).add(Registries.TRADE_SET, TradeSets::bootstrap);

    private static void validateThatAllBiomeFeaturesHaveBiomeFilter(HolderLookup.Provider provider) {
        VanillaRegistries.validateThatAllBiomeFeaturesHaveBiomeFilter(provider.lookupOrThrow(Registries.PLACED_FEATURE), (HolderLookup<Biome>)provider.lookupOrThrow(Registries.BIOME));
    }

    public static void validateThatAllBiomeFeaturesHaveBiomeFilter(HolderGetter<PlacedFeature> placedFeatures, HolderLookup<Biome> biomes) {
        biomes.listElements().forEach(biome -> {
            Identifier biomeKey = biome.key().identifier();
            List<HolderSet<PlacedFeature>> biomeFeatures = ((Biome)biome.value()).getGenerationSettings().features();
            biomeFeatures.stream().flatMap(HolderSet::stream).forEach(feature -> feature.unwrap().ifLeft(key -> {
                Holder.Reference value = placedFeatures.getOrThrow((ResourceKey<PlacedFeature>)key);
                if (!VanillaRegistries.validatePlacedFeature((PlacedFeature)value.value())) {
                    Util.logAndPauseIfInIde("Placed feature " + String.valueOf(key.identifier()) + " in biome " + String.valueOf(biomeKey) + " is missing BiomeFilter.biome()");
                }
            }).ifRight(value -> {
                if (!VanillaRegistries.validatePlacedFeature(value)) {
                    Util.logAndPauseIfInIde("Placed inline feature in biome " + String.valueOf(biome) + " is missing BiomeFilter.biome()");
                }
            }));
        });
    }

    private static boolean validatePlacedFeature(PlacedFeature value) {
        return value.placement().contains(BiomeFilter.biome());
    }

    public static HolderLookup.Provider createLookup() {
        RegistryAccess.Frozen staticRegistries = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        HolderLookup.Provider newRegistries = BUILDER.build(staticRegistries);
        VanillaRegistries.validateThatAllBiomeFeaturesHaveBiomeFilter(newRegistries);
        return newRegistries;
    }
}

