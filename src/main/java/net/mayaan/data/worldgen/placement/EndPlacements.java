/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.worldgen.placement;

import net.mayaan.core.Holder;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.data.worldgen.features.EndFeatures;
import net.mayaan.data.worldgen.placement.PlacementUtils;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.valueproviders.UniformInt;
import net.mayaan.world.level.levelgen.VerticalAnchor;
import net.mayaan.world.level.levelgen.feature.ConfiguredFeature;
import net.mayaan.world.level.levelgen.placement.BiomeFilter;
import net.mayaan.world.level.levelgen.placement.CountPlacement;
import net.mayaan.world.level.levelgen.placement.FixedPlacement;
import net.mayaan.world.level.levelgen.placement.HeightRangePlacement;
import net.mayaan.world.level.levelgen.placement.InSquarePlacement;
import net.mayaan.world.level.levelgen.placement.PlacedFeature;
import net.mayaan.world.level.levelgen.placement.RandomOffsetPlacement;
import net.mayaan.world.level.levelgen.placement.RarityFilter;

public class EndPlacements {
    public static final ResourceKey<PlacedFeature> END_PLATFORM = PlacementUtils.createKey("end_platform");
    public static final ResourceKey<PlacedFeature> END_SPIKE = PlacementUtils.createKey("end_spike");
    public static final ResourceKey<PlacedFeature> END_GATEWAY_RETURN = PlacementUtils.createKey("end_gateway_return");
    public static final ResourceKey<PlacedFeature> CHORUS_PLANT = PlacementUtils.createKey("chorus_plant");
    public static final ResourceKey<PlacedFeature> END_ISLAND_DECORATED = PlacementUtils.createKey("end_island_decorated");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder.Reference<ConfiguredFeature<?, ?>> endPlatform = configuredFeatures.getOrThrow(EndFeatures.END_PLATFORM);
        Holder.Reference<ConfiguredFeature<?, ?>> endSpike = configuredFeatures.getOrThrow(EndFeatures.END_SPIKE);
        Holder.Reference<ConfiguredFeature<?, ?>> endGatewayReturn = configuredFeatures.getOrThrow(EndFeatures.END_GATEWAY_RETURN);
        Holder.Reference<ConfiguredFeature<?, ?>> chorusPlant = configuredFeatures.getOrThrow(EndFeatures.CHORUS_PLANT);
        Holder.Reference<ConfiguredFeature<?, ?>> endIsland = configuredFeatures.getOrThrow(EndFeatures.END_ISLAND);
        PlacementUtils.register(context, END_PLATFORM, endPlatform, FixedPlacement.of(ServerLevel.END_SPAWN_POINT.below()), BiomeFilter.biome());
        PlacementUtils.register(context, END_SPIKE, endSpike, BiomeFilter.biome());
        PlacementUtils.register(context, END_GATEWAY_RETURN, endGatewayReturn, RarityFilter.onAverageOnceEvery(700), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, RandomOffsetPlacement.vertical(UniformInt.of(3, 9)), BiomeFilter.biome());
        PlacementUtils.register(context, CHORUS_PLANT, chorusPlant, CountPlacement.of(UniformInt.of(0, 4)), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
        PlacementUtils.register(context, END_ISLAND_DECORATED, endIsland, RarityFilter.onAverageOnceEvery(14), PlacementUtils.countExtra(1, 0.25f, 1), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(55), VerticalAnchor.absolute(70)), BiomeFilter.biome());
    }
}

