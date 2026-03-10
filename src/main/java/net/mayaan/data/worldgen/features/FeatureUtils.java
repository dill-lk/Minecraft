/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.worldgen.features;

import java.util.List;
import net.mayaan.core.Direction;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.data.worldgen.features.AquaticFeatures;
import net.mayaan.data.worldgen.features.CaveFeatures;
import net.mayaan.data.worldgen.features.EndFeatures;
import net.mayaan.data.worldgen.features.MiscOverworldFeatures;
import net.mayaan.data.worldgen.features.NetherFeatures;
import net.mayaan.data.worldgen.features.OreFeatures;
import net.mayaan.data.worldgen.features.PileFeatures;
import net.mayaan.data.worldgen.features.TreeFeatures;
import net.mayaan.data.worldgen.features.VegetationFeatures;
import net.mayaan.data.worldgen.placement.PlacementUtils;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.levelgen.blockpredicates.BlockPredicate;
import net.mayaan.world.level.levelgen.feature.ConfiguredFeature;
import net.mayaan.world.level.levelgen.feature.Feature;
import net.mayaan.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.mayaan.world.level.levelgen.placement.PlacedFeature;

public class FeatureUtils {
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        AquaticFeatures.bootstrap(context);
        CaveFeatures.bootstrap(context);
        EndFeatures.bootstrap(context);
        MiscOverworldFeatures.bootstrap(context);
        NetherFeatures.bootstrap(context);
        OreFeatures.bootstrap(context);
        PileFeatures.bootstrap(context);
        TreeFeatures.bootstrap(context);
        VegetationFeatures.bootstrap(context);
    }

    private static BlockPredicate simplePatchPredicate(List<Block> allowedOn) {
        BlockPredicate predicate = !allowedOn.isEmpty() ? BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), allowedOn)) : BlockPredicate.ONLY_IN_AIR_PREDICATE;
        return predicate;
    }

    public static RandomPatchConfiguration simpleRandomPatchConfiguration(int tries, Holder<PlacedFeature> feature) {
        return new RandomPatchConfiguration(tries, 7, 3, feature);
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F feature, FC config, List<Block> allowedOn, int tries) {
        return FeatureUtils.simpleRandomPatchConfiguration(tries, PlacementUtils.filtered(feature, config, FeatureUtils.simplePatchPredicate(allowedOn)));
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F feature, FC config, List<Block> allowedOn) {
        return FeatureUtils.simplePatchConfiguration(feature, config, allowedOn, 96);
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F feature, FC config) {
        return FeatureUtils.simplePatchConfiguration(feature, config, List.of(), 96);
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.withDefaultNamespace(name));
    }

    public static void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> id, Feature<NoneFeatureConfiguration> feature) {
        FeatureUtils.register(context, id, feature, FeatureConfiguration.NONE);
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> id, F feature, FC config) {
        context.register(id, new ConfiguredFeature<FC, F>(feature, config));
    }
}

