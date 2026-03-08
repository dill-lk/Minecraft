/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.village.poi;

import java.util.Set;
import java.util.function.Predicate;
import net.mayaan.core.Holder;
import net.mayaan.world.level.block.state.BlockState;

public record PoiType(Set<BlockState> matchingStates, int maxTickets, int validRange) {
    public static final Predicate<Holder<PoiType>> NONE = poiType -> false;

    public PoiType {
        matchingStates = Set.copyOf(matchingStates);
    }

    public boolean is(BlockState state) {
        return this.matchingStates.contains(state);
    }
}

