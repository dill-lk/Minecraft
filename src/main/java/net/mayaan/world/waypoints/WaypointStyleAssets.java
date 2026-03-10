/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.waypoints;

import net.mayaan.core.Registry;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.waypoints.WaypointStyleAsset;

public interface WaypointStyleAssets {
    public static final ResourceKey<? extends Registry<WaypointStyleAsset>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("waypoint_style_asset"));
    public static final ResourceKey<WaypointStyleAsset> DEFAULT = WaypointStyleAssets.createId("default");
    public static final ResourceKey<WaypointStyleAsset> BOWTIE = WaypointStyleAssets.createId("bowtie");

    public static ResourceKey<WaypointStyleAsset> createId(String name) {
        return ResourceKey.create(ROOT_ID, Identifier.withDefaultNamespace(name));
    }
}

