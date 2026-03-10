/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources;

import java.io.IOException;
import net.mayaan.client.resources.LegacyStuffWrapper;
import net.mayaan.resources.Identifier;
import net.mayaan.server.packs.resources.ResourceManager;
import net.mayaan.server.packs.resources.SimplePreparableReloadListener;
import net.mayaan.util.profiling.ProfilerFiller;
import net.mayaan.world.level.DryFoliageColor;

public class DryFoliageColorReloadListener
extends SimplePreparableReloadListener<int[]> {
    private static final Identifier LOCATION = Identifier.withDefaultNamespace("textures/colormap/dry_foliage.png");

    @Override
    protected int[] prepare(ResourceManager manager, ProfilerFiller profiler) {
        try {
            return LegacyStuffWrapper.getPixels(manager, LOCATION);
        }
        catch (IOException e) {
            throw new IllegalStateException("Failed to load dry foliage color texture", e);
        }
    }

    @Override
    protected void apply(int[] pixels, ResourceManager manager, ProfilerFiller profiler) {
        DryFoliageColor.init(pixels);
    }
}

