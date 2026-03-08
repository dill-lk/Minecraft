/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.debugchart;

import net.mayaan.util.debug.DebugSubscription;
import net.mayaan.util.debug.DebugSubscriptions;

public enum RemoteDebugSampleType {
    TICK_TIME(DebugSubscriptions.DEDICATED_SERVER_TICK_TIME);

    private final DebugSubscription<?> subscription;

    private RemoteDebugSampleType(DebugSubscription<?> subscription) {
        this.subscription = subscription;
    }

    public DebugSubscription<?> subscription() {
        return this.subscription;
    }
}

