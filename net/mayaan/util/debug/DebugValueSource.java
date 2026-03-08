/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.util.debug;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.debug.DebugSubscription;
import org.jspecify.annotations.Nullable;

public interface DebugValueSource {
    public void registerDebugValues(ServerLevel var1, Registration var2);

    public static interface ValueGetter<T> {
        public @Nullable T get();
    }

    public static interface Registration {
        public <T> void register(DebugSubscription<T> var1, ValueGetter<T> var2);
    }
}

