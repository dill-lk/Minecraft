/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 */
package net.mayaan.data.worldgen.features;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.data.worldgen.features.FeatureUtils;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.level.levelgen.feature.ConfiguredFeature;
import net.mayaan.world.level.levelgen.feature.EndSpikeFeature;
import net.mayaan.world.level.levelgen.feature.Feature;
import net.mayaan.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
import net.mayaan.world.level.levelgen.feature.configurations.EndSpikeConfiguration;

public class EndFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> END_PLATFORM = FeatureUtils.createKey("end_platform");
    public static final ResourceKey<ConfiguredFeature<?, ?>> END_SPIKE = FeatureUtils.createKey("end_spike");
    public static final ResourceKey<ConfiguredFeature<?, ?>> END_GATEWAY_RETURN = FeatureUtils.createKey("end_gateway_return");
    public static final ResourceKey<ConfiguredFeature<?, ?>> END_GATEWAY_DELAYED = FeatureUtils.createKey("end_gateway_delayed");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CHORUS_PLANT = FeatureUtils.createKey("chorus_plant");
    public static final ResourceKey<ConfiguredFeature<?, ?>> END_ISLAND = FeatureUtils.createKey("end_island");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        FeatureUtils.register(context, END_PLATFORM, Feature.END_PLATFORM);
        FeatureUtils.register(context, END_SPIKE, Feature.END_SPIKE, new EndSpikeConfiguration(false, (List<EndSpikeFeature.EndSpike>)ImmutableList.of(), null));
        FeatureUtils.register(context, END_GATEWAY_RETURN, Feature.END_GATEWAY, EndGatewayConfiguration.knownExit(ServerLevel.END_SPAWN_POINT, true));
        FeatureUtils.register(context, END_GATEWAY_DELAYED, Feature.END_GATEWAY, EndGatewayConfiguration.delayedExitSearch());
        FeatureUtils.register(context, CHORUS_PLANT, Feature.CHORUS_PLANT);
        FeatureUtils.register(context, END_ISLAND, Feature.END_ISLAND);
    }
}

