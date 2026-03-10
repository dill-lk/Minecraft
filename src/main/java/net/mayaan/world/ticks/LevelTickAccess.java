/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.ticks;

import net.mayaan.core.BlockPos;
import net.mayaan.world.ticks.TickAccess;

public interface LevelTickAccess<T>
extends TickAccess<T> {
    public boolean willTickThisTick(BlockPos var1, T var2);
}

