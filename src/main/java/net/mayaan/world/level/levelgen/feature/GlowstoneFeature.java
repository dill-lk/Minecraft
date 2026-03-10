/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.feature.Feature;
import net.mayaan.world.level.levelgen.feature.FeaturePlaceContext;
import net.mayaan.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class GlowstoneFeature
extends Feature<NoneFeatureConfiguration> {
    public GlowstoneFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        if (!level.isEmptyBlock(origin)) {
            return false;
        }
        BlockState aboveState = level.getBlockState(origin.above());
        if (!(aboveState.is(Blocks.NETHERRACK) || aboveState.is(Blocks.BASALT) || aboveState.is(Blocks.BLACKSTONE))) {
            return false;
        }
        level.setBlock(origin, Blocks.GLOWSTONE.defaultBlockState(), 2);
        for (int i = 0; i < 1500; ++i) {
            BlockPos placePos = origin.offset(random.nextInt(8) - random.nextInt(8), -random.nextInt(12), random.nextInt(8) - random.nextInt(8));
            if (!level.getBlockState(placePos).isAir()) continue;
            int neighbours = 0;
            for (Direction direction : Direction.values()) {
                if (level.getBlockState(placePos.relative(direction)).is(Blocks.GLOWSTONE)) {
                    ++neighbours;
                }
                if (neighbours > 1) break;
            }
            if (neighbours != true) continue;
            level.setBlock(placePos, Blocks.GLOWSTONE.defaultBlockState(), 2);
        }
        return true;
    }
}

