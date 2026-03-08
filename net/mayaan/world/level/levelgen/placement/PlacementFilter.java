/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.levelgen.placement;

import java.util.stream.Stream;
import net.mayaan.core.BlockPos;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.levelgen.placement.PlacementContext;
import net.mayaan.world.level.levelgen.placement.PlacementModifier;

public abstract class PlacementFilter
extends PlacementModifier {
    @Override
    public final Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
        if (this.shouldPlace(context, random, origin)) {
            return Stream.of(origin);
        }
        return Stream.of(new BlockPos[0]);
    }

    protected abstract boolean shouldPlace(PlacementContext var1, RandomSource var2, BlockPos var3);
}

