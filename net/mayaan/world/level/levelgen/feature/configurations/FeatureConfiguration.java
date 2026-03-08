/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.levelgen.feature.configurations;

import java.util.stream.Stream;
import net.mayaan.world.level.levelgen.feature.ConfiguredFeature;
import net.mayaan.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public interface FeatureConfiguration {
    public static final NoneFeatureConfiguration NONE = NoneFeatureConfiguration.INSTANCE;

    default public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return Stream.empty();
    }
}

