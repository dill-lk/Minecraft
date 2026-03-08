/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.DataResult
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.util.RandomSource;
import net.mayaan.util.random.WeightedList;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.mayaan.world.level.levelgen.feature.stateproviders.BlockStateProviderType;

public class WeightedStateProvider
extends BlockStateProvider {
    public static final MapCodec<WeightedStateProvider> CODEC = WeightedList.nonEmptyCodec(BlockState.CODEC).comapFlatMap(WeightedStateProvider::create, p -> p.weightedList).fieldOf("entries");
    private final WeightedList<BlockState> weightedList;

    private static DataResult<WeightedStateProvider> create(WeightedList<BlockState> weightedList) {
        if (weightedList.isEmpty()) {
            return DataResult.error(() -> "WeightedStateProvider with no states");
        }
        return DataResult.success((Object)new WeightedStateProvider(weightedList));
    }

    public WeightedStateProvider(WeightedList<BlockState> weightedList) {
        this.weightedList = weightedList;
    }

    public WeightedStateProvider(WeightedList.Builder<BlockState> weightedList) {
        this(weightedList.build());
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.WEIGHTED_STATE_PROVIDER;
    }

    @Override
    public BlockState getState(WorldGenLevel level, RandomSource random, BlockPos pos) {
        return this.weightedList.getRandomOrThrow(random);
    }
}

