/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.mayaan.core.BlockPos;
import net.mayaan.tags.BlockTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.ChorusFlowerBlock;
import net.mayaan.world.level.levelgen.feature.Feature;
import net.mayaan.world.level.levelgen.feature.FeaturePlaceContext;
import net.mayaan.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ChorusPlantFeature
extends Feature<NoneFeatureConfiguration> {
    public ChorusPlantFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        if (level.isEmptyBlock(origin) && level.getBlockState(origin.below()).is(BlockTags.SUPPORTS_CHORUS_PLANT)) {
            ChorusFlowerBlock.generatePlant(level, origin, random, 8);
            return true;
        }
        return false;
    }
}

