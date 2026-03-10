/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.ticks;

import java.util.function.Function;
import net.mayaan.core.BlockPos;
import net.mayaan.world.ticks.LevelTickAccess;
import net.mayaan.world.ticks.ScheduledTick;
import net.mayaan.world.ticks.TickContainerAccess;

public class WorldGenTickAccess<T>
implements LevelTickAccess<T> {
    private final Function<BlockPos, TickContainerAccess<T>> containerGetter;

    public WorldGenTickAccess(Function<BlockPos, TickContainerAccess<T>> containerGetter) {
        this.containerGetter = containerGetter;
    }

    @Override
    public boolean hasScheduledTick(BlockPos pos, T type) {
        return this.containerGetter.apply(pos).hasScheduledTick(pos, type);
    }

    @Override
    public void schedule(ScheduledTick<T> tick) {
        this.containerGetter.apply(tick.pos()).schedule(tick);
    }

    @Override
    public boolean willTickThisTick(BlockPos pos, T type) {
        return false;
    }

    @Override
    public int count() {
        return 0;
    }
}

