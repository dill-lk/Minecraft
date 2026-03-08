/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

public class RandomPatchFeature
extends Feature<RandomPatchConfiguration> {
    public RandomPatchFeature(Codec<RandomPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<RandomPatchConfiguration> context) {
        RandomPatchConfiguration config = context.config();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();
        int placed = 0;
        BlockPos.MutableBlockPos grassPos = new BlockPos.MutableBlockPos();
        int xzBound = config.xzSpread() + 1;
        int yBound = config.ySpread() + 1;
        for (int i = 0; i < config.tries(); ++i) {
            grassPos.setWithOffset(origin, random.nextInt(xzBound) - random.nextInt(xzBound), random.nextInt(yBound) - random.nextInt(yBound), random.nextInt(xzBound) - random.nextInt(xzBound));
            if (!config.feature().value().place(level, context.chunkGenerator(), random, grassPos)) continue;
            ++placed;
        }
        return placed > 0;
    }
}

