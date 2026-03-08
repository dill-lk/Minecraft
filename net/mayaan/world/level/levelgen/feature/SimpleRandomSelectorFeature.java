/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.mayaan.core.BlockPos;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.levelgen.feature.Feature;
import net.mayaan.world.level.levelgen.feature.FeaturePlaceContext;
import net.mayaan.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.mayaan.world.level.levelgen.placement.PlacedFeature;

public class SimpleRandomSelectorFeature
extends Feature<SimpleRandomFeatureConfiguration> {
    public SimpleRandomSelectorFeature(Codec<SimpleRandomFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleRandomFeatureConfiguration> context) {
        RandomSource random = context.random();
        SimpleRandomFeatureConfiguration config = context.config();
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        int index = random.nextInt(config.features.size());
        PlacedFeature feature = config.features.get(index).value();
        return feature.place(level, chunkGenerator, random, origin);
    }
}

