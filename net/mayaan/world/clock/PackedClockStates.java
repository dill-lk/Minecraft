/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.clock;

import com.mojang.serialization.Codec;
import java.util.Map;
import net.mayaan.core.Holder;
import net.mayaan.world.clock.ClockState;
import net.mayaan.world.clock.WorldClock;

public record PackedClockStates(Map<Holder<WorldClock>, ClockState> clocks) {
    public static final PackedClockStates EMPTY = new PackedClockStates(Map.of());
    public static final Codec<PackedClockStates> CODEC = Codec.unboundedMap(WorldClock.CODEC, ClockState.CODEC).xmap(PackedClockStates::new, PackedClockStates::clocks);
}

