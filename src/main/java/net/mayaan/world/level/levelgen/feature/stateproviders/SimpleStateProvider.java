/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.mayaan.world.level.levelgen.feature.stateproviders.BlockStateProviderType;

public class SimpleStateProvider
extends BlockStateProvider {
    public static final MapCodec<SimpleStateProvider> CODEC = BlockState.CODEC.fieldOf("state").xmap(SimpleStateProvider::new, p -> p.state);
    private final BlockState state;

    protected SimpleStateProvider(BlockState state) {
        this.state = state;
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.SIMPLE_STATE_PROVIDER;
    }

    @Override
    public BlockState getState(WorldGenLevel level, RandomSource random, BlockPos pos) {
        return this.state;
    }
}

