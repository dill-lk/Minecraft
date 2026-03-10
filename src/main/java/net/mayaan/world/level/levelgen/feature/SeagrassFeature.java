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
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.TallSeagrassBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.DoubleBlockHalf;
import net.mayaan.world.level.levelgen.Heightmap;
import net.mayaan.world.level.levelgen.feature.Feature;
import net.mayaan.world.level.levelgen.feature.FeaturePlaceContext;
import net.mayaan.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class SeagrassFeature
extends Feature<ProbabilityFeatureConfiguration> {
    public SeagrassFeature(Codec<ProbabilityFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> context) {
        boolean placedAny = false;
        RandomSource random = context.random();
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        ProbabilityFeatureConfiguration config = context.config();
        int x = random.nextInt(8) - random.nextInt(8);
        int z = random.nextInt(8) - random.nextInt(8);
        int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, origin.getX() + x, origin.getZ() + z);
        BlockPos grassPos = new BlockPos(origin.getX() + x, y, origin.getZ() + z);
        if (level.getBlockState(grassPos).is(Blocks.WATER)) {
            BlockState state;
            boolean isTall = random.nextDouble() < (double)config.probability;
            BlockState blockState = state = isTall ? Blocks.TALL_SEAGRASS.defaultBlockState() : Blocks.SEAGRASS.defaultBlockState();
            if (state.canSurvive(level, grassPos)) {
                if (isTall) {
                    BlockState upperState = (BlockState)state.setValue(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER);
                    BlockPos above = grassPos.above();
                    if (level.getBlockState(above).is(Blocks.WATER)) {
                        level.setBlock(grassPos, state, 2);
                        level.setBlock(above, upperState, 2);
                    }
                } else {
                    level.setBlock(grassPos, state, 2);
                }
                placedAny = true;
            }
        }
        return placedAny;
    }
}

