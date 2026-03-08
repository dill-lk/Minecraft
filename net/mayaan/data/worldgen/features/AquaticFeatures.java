/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.worldgen.features;

import net.mayaan.core.HolderSet;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.data.worldgen.features.FeatureUtils;
import net.mayaan.data.worldgen.placement.PlacementUtils;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.levelgen.feature.ConfiguredFeature;
import net.mayaan.world.level.levelgen.feature.Feature;
import net.mayaan.world.level.levelgen.feature.configurations.CountConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.mayaan.world.level.levelgen.placement.PlacementModifier;

public class AquaticFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SEAGRASS_SHORT = FeatureUtils.createKey("seagrass_short");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SEAGRASS_SLIGHTLY_LESS_SHORT = FeatureUtils.createKey("seagrass_slightly_less_short");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SEAGRASS_MID = FeatureUtils.createKey("seagrass_mid");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SEAGRASS_TALL = FeatureUtils.createKey("seagrass_tall");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SEA_PICKLE = FeatureUtils.createKey("sea_pickle");
    public static final ResourceKey<ConfiguredFeature<?, ?>> KELP = FeatureUtils.createKey("kelp");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WARM_OCEAN_VEGETATION = FeatureUtils.createKey("warm_ocean_vegetation");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        FeatureUtils.register(context, SEAGRASS_SHORT, Feature.SEAGRASS, new ProbabilityFeatureConfiguration(0.3f));
        FeatureUtils.register(context, SEAGRASS_SLIGHTLY_LESS_SHORT, Feature.SEAGRASS, new ProbabilityFeatureConfiguration(0.4f));
        FeatureUtils.register(context, SEAGRASS_MID, Feature.SEAGRASS, new ProbabilityFeatureConfiguration(0.6f));
        FeatureUtils.register(context, SEAGRASS_TALL, Feature.SEAGRASS, new ProbabilityFeatureConfiguration(0.8f));
        FeatureUtils.register(context, SEA_PICKLE, Feature.SEA_PICKLE, new CountConfiguration(20));
        FeatureUtils.register(context, KELP, Feature.KELP);
        FeatureUtils.register(context, WARM_OCEAN_VEGETATION, Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(PlacementUtils.inlinePlaced(Feature.CORAL_TREE, FeatureConfiguration.NONE, new PlacementModifier[0]), PlacementUtils.inlinePlaced(Feature.CORAL_CLAW, FeatureConfiguration.NONE, new PlacementModifier[0]), PlacementUtils.inlinePlaced(Feature.CORAL_MUSHROOM, FeatureConfiguration.NONE, new PlacementModifier[0]))));
    }
}

