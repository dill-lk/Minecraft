/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.blockpredicates;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.levelgen.blockpredicates.BlockPredicate;
import net.mayaan.world.level.levelgen.blockpredicates.BlockPredicateType;

class TrueBlockPredicate
implements BlockPredicate {
    public static final TrueBlockPredicate INSTANCE = new TrueBlockPredicate();
    public static final MapCodec<TrueBlockPredicate> CODEC = MapCodec.unit(() -> INSTANCE);

    private TrueBlockPredicate() {
    }

    @Override
    public boolean test(WorldGenLevel level, BlockPos origin) {
        return true;
    }

    @Override
    public BlockPredicateType<?> type() {
        return BlockPredicateType.TRUE;
    }
}

