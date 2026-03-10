/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.mayaan.core.BlockPos;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.levelgen.feature.Feature;
import net.mayaan.world.level.levelgen.feature.FeaturePlaceContext;
import net.mayaan.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class EndIslandFeature
extends Feature<NoneFeatureConfiguration> {
    public EndIslandFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        float size = (float)random.nextInt(3) + 4.0f;
        int y = 0;
        while (size > 0.5f) {
            for (int x = Mth.floor(-size); x <= Mth.ceil(size); ++x) {
                for (int z = Mth.floor(-size); z <= Mth.ceil(size); ++z) {
                    if (!((float)(x * x + z * z) <= (size + 1.0f) * (size + 1.0f))) continue;
                    this.setBlock(level, origin.offset(x, y, z), Blocks.END_STONE.defaultBlockState());
                }
            }
            size -= (float)random.nextInt(2) + 0.5f;
            --y;
        }
        return true;
    }
}

