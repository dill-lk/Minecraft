/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.data.models;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import net.mayaan.client.resources.WaypointStyle;
import net.mayaan.data.CachedOutput;
import net.mayaan.data.DataProvider;
import net.mayaan.data.PackOutput;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.waypoints.WaypointStyleAsset;
import net.mayaan.world.waypoints.WaypointStyleAssets;

public class WaypointStyleProvider
implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public WaypointStyleProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "waypoint_style");
    }

    private static void bootstrap(BiConsumer<ResourceKey<WaypointStyleAsset>, WaypointStyle> consumer) {
        consumer.accept(WaypointStyleAssets.DEFAULT, new WaypointStyle(128, 332, List.of(Identifier.withDefaultNamespace("default_0"), Identifier.withDefaultNamespace("default_1"), Identifier.withDefaultNamespace("default_2"), Identifier.withDefaultNamespace("default_3"))));
        consumer.accept(WaypointStyleAssets.BOWTIE, new WaypointStyle(64, 332, List.of(Identifier.withDefaultNamespace("bowtie"), Identifier.withDefaultNamespace("default_0"), Identifier.withDefaultNamespace("default_1"), Identifier.withDefaultNamespace("default_2"), Identifier.withDefaultNamespace("default_3"))));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        HashMap waypointStyles = new HashMap();
        WaypointStyleProvider.bootstrap((id, asset) -> {
            if (waypointStyles.putIfAbsent(id, asset) != null) {
                throw new IllegalStateException("Tried to register waypoint style twice for id: " + String.valueOf(id));
            }
        });
        return DataProvider.saveAll(cache, WaypointStyle.CODEC, this.pathProvider::json, waypointStyles);
    }

    @Override
    public String getName() {
        return "Waypoint Style Definitions";
    }
}

