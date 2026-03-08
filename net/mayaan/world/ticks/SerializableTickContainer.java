/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.ticks;

import java.util.List;
import net.mayaan.world.ticks.SavedTick;

public interface SerializableTickContainer<T> {
    public List<SavedTick<T>> pack(long var1);
}

