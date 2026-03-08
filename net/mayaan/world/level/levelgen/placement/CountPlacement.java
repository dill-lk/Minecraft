/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.placement;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.util.RandomSource;
import net.mayaan.util.valueproviders.ConstantInt;
import net.mayaan.util.valueproviders.IntProvider;
import net.mayaan.world.level.levelgen.placement.PlacementModifierType;
import net.mayaan.world.level.levelgen.placement.RepeatingPlacement;

public class CountPlacement
extends RepeatingPlacement {
    public static final MapCodec<CountPlacement> CODEC = IntProvider.codec(0, 256).fieldOf("count").xmap(CountPlacement::new, c -> c.count);
    private final IntProvider count;

    private CountPlacement(IntProvider count) {
        this.count = count;
    }

    public static CountPlacement of(IntProvider count) {
        return new CountPlacement(count);
    }

    public static CountPlacement of(int count) {
        return CountPlacement.of(ConstantInt.of(count));
    }

    @Override
    protected int count(RandomSource random, BlockPos origin) {
        return this.count.sample(random);
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.COUNT;
    }
}

