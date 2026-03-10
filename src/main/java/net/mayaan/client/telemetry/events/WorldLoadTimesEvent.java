/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.telemetry.events;

import java.time.Duration;
import net.mayaan.client.telemetry.TelemetryEventSender;
import net.mayaan.client.telemetry.TelemetryEventType;
import net.mayaan.client.telemetry.TelemetryProperty;
import org.jspecify.annotations.Nullable;

public class WorldLoadTimesEvent {
    private final boolean newWorld;
    private final @Nullable Duration worldLoadDuration;

    public WorldLoadTimesEvent(boolean newWorld, @Nullable Duration worldLoadDuration) {
        this.worldLoadDuration = worldLoadDuration;
        this.newWorld = newWorld;
    }

    public void send(TelemetryEventSender eventSender) {
        if (this.worldLoadDuration != null) {
            eventSender.send(TelemetryEventType.WORLD_LOAD_TIMES, event -> {
                event.put(TelemetryProperty.WORLD_LOAD_TIME_MS, (int)this.worldLoadDuration.toMillis());
                event.put(TelemetryProperty.NEW_WORLD, this.newWorld);
            });
        }
    }
}

