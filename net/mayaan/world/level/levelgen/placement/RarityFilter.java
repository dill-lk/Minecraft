/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.placement;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.levelgen.placement.PlacementContext;
import net.mayaan.world.level.levelgen.placement.PlacementFilter;
import net.mayaan.world.level.levelgen.placement.PlacementModifierType;

public class RarityFilter
extends PlacementFilter {
    public static final MapCodec<RarityFilter> CODEC = ExtraCodecs.POSITIVE_INT.fieldOf("chance").xmap(RarityFilter::new, c -> c.chance);
    private final int chance;

    private RarityFilter(int chance) {
        this.chance = chance;
    }

    public static RarityFilter onAverageOnceEvery(int chance) {
        return new RarityFilter(chance);
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) {
        return random.nextFloat() < 1.0f / (float)this.chance;
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.RARITY_FILTER;
    }
}

