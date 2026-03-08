/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.mayaan.client.renderer.texture.MissingTextureAtlasSprite;
import net.mayaan.client.resources.WaypointStyle;
import net.mayaan.resources.FileToIdConverter;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.packs.resources.ResourceManager;
import net.mayaan.server.packs.resources.SimpleJsonResourceReloadListener;
import net.mayaan.util.profiling.ProfilerFiller;
import net.mayaan.world.waypoints.WaypointStyleAsset;
import net.mayaan.world.waypoints.WaypointStyleAssets;

public class WaypointStyleManager
extends SimpleJsonResourceReloadListener<WaypointStyle> {
    private static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json("waypoint_style");
    private static final WaypointStyle MISSING = new WaypointStyle(0, 1, List.of(MissingTextureAtlasSprite.getLocation()));
    private Map<ResourceKey<WaypointStyleAsset>, WaypointStyle> waypointStyles = Map.of();

    public WaypointStyleManager() {
        super(WaypointStyle.CODEC, ASSET_LISTER);
    }

    @Override
    protected void apply(Map<Identifier, WaypointStyle> preparations, ResourceManager manager, ProfilerFiller profiler) {
        this.waypointStyles = preparations.entrySet().stream().collect(Collectors.toUnmodifiableMap(e -> ResourceKey.create(WaypointStyleAssets.ROOT_ID, (Identifier)e.getKey()), Map.Entry::getValue));
    }

    public WaypointStyle get(ResourceKey<WaypointStyleAsset> id) {
        return this.waypointStyles.getOrDefault(id, MISSING);
    }
}

