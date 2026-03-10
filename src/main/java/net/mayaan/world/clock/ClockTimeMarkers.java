/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.clock;

import net.mayaan.core.Registry;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.clock.ClockTimeMarker;

public interface ClockTimeMarkers {
    public static final ResourceKey<? extends Registry<ClockTimeMarker>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("clock_time_marker"));
    public static final ResourceKey<ClockTimeMarker> DAY = ClockTimeMarkers.createKey("day");
    public static final ResourceKey<ClockTimeMarker> NOON = ClockTimeMarkers.createKey("noon");
    public static final ResourceKey<ClockTimeMarker> NIGHT = ClockTimeMarkers.createKey("night");
    public static final ResourceKey<ClockTimeMarker> MIDNIGHT = ClockTimeMarkers.createKey("midnight");
    public static final ResourceKey<ClockTimeMarker> WAKE_UP_FROM_SLEEP = ClockTimeMarkers.createKey("wake_up_from_sleep");
    public static final ResourceKey<ClockTimeMarker> ROLL_VILLAGE_SIEGE = ClockTimeMarkers.createKey("roll_village_siege");

    public static ResourceKey<ClockTimeMarker> createKey(String name) {
        return ResourceKey.create(ROOT_ID, Identifier.withDefaultNamespace(name));
    }
}

