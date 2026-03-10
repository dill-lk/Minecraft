/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.ticks;

import net.mayaan.core.BlockPos;
import net.mayaan.world.ticks.ScheduledTick;

public interface TickAccess<T> {
    public void schedule(ScheduledTick<T> var1);

    public boolean hasScheduledTick(BlockPos var1, T var2);

    public int count();
}

