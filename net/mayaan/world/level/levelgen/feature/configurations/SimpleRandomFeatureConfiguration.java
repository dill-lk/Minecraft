/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import java.util.stream.Stream;
import net.mayaan.core.HolderSet;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.world.level.levelgen.feature.ConfiguredFeature;
import net.mayaan.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.mayaan.world.level.levelgen.placement.PlacedFeature;

public class SimpleRandomFeatureConfiguration
implements FeatureConfiguration {
    public static final Codec<SimpleRandomFeatureConfiguration> CODEC = ExtraCodecs.nonEmptyHolderSet(PlacedFeature.LIST_CODEC).fieldOf("features").xmap(SimpleRandomFeatureConfiguration::new, c -> c.features).codec();
    public final HolderSet<PlacedFeature> features;

    public SimpleRandomFeatureConfiguration(HolderSet<PlacedFeature> features) {
        this.features = features;
    }

    @Override
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return this.features.stream().flatMap(f -> ((PlacedFeature)f.value()).getFeatures());
    }
}

