/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.levelgen.feature.trunkplacers;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.BiConsumer;
import net.mayaan.core.BlockPos;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.mayaan.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.mayaan.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.mayaan.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public class StraightTrunkPlacer
extends TrunkPlacer {
    public static final MapCodec<StraightTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> StraightTrunkPlacer.trunkPlacerParts(i).apply((Applicative)i, StraightTrunkPlacer::new));

    public StraightTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.STRAIGHT_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
        StraightTrunkPlacer.placeBelowTrunkBlock(level, trunkSetter, random, origin.below(), config);
        for (int y = 0; y < treeHeight; ++y) {
            this.placeLog(level, trunkSetter, random, origin.above(y), config);
        }
        return ImmutableList.of((Object)new FoliagePlacer.FoliageAttachment(origin.above(treeHeight), 0, false));
    }
}

