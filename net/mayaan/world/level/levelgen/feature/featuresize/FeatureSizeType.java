/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.feature.featuresize;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.world.level.levelgen.feature.featuresize.FeatureSize;
import net.mayaan.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.mayaan.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;

public class FeatureSizeType<P extends FeatureSize> {
    public static final FeatureSizeType<TwoLayersFeatureSize> TWO_LAYERS_FEATURE_SIZE = FeatureSizeType.register("two_layers_feature_size", TwoLayersFeatureSize.CODEC);
    public static final FeatureSizeType<ThreeLayersFeatureSize> THREE_LAYERS_FEATURE_SIZE = FeatureSizeType.register("three_layers_feature_size", ThreeLayersFeatureSize.CODEC);
    private final MapCodec<P> codec;

    private static <P extends FeatureSize> FeatureSizeType<P> register(String name, MapCodec<P> codec) {
        return Registry.register(BuiltInRegistries.FEATURE_SIZE_TYPE, name, new FeatureSizeType<P>(codec));
    }

    private FeatureSizeType(MapCodec<P> codec) {
        this.codec = codec;
    }

    public MapCodec<P> codec() {
        return this.codec;
    }
}

