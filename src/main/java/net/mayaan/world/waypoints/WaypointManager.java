/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.waypoints;

import net.mayaan.world.waypoints.Waypoint;

public interface WaypointManager<T extends Waypoint> {
    public void trackWaypoint(T var1);

    public void updateWaypoint(T var1);

    public void untrackWaypoint(T var1);
}

