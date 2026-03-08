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
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.VineBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.feature.Feature;
import net.mayaan.world.level.levelgen.feature.FeaturePlaceContext;
import net.mayaan.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class VinesFeature
extends Feature<NoneFeatureConfiguration> {
    public VinesFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        context.config();
        if (!level.isEmptyBlock(origin)) {
            return false;
        }
        for (Direction direction : Direction.values()) {
            if (direction == Direction.DOWN || !VineBlock.isAcceptableNeighbour(level, origin.relative(direction), direction)) continue;
            level.setBlock(origin, (BlockState)Blocks.VINE.defaultBlockState().setValue(VineBlock.getPropertyForFace(direction), true), 2);
            return true;
        }
        return false;
    }
}

