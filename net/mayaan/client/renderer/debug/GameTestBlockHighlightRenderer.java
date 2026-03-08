/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.mayaan.client.renderer.debug;

import com.google.common.collect.Maps;
import java.util.Map;
import net.mayaan.core.BlockPos;
import net.mayaan.gizmos.GizmoStyle;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.gizmos.TextGizmo;
import net.mayaan.util.Util;
import net.mayaan.world.phys.Vec3;

public class GameTestBlockHighlightRenderer {
    private static final int SHOW_POS_DURATION_MS = 10000;
    private static final float PADDING = 0.02f;
    private final Map<BlockPos, Marker> markers = Maps.newHashMap();

    public void highlightPos(BlockPos absolutePos, BlockPos relativePos) {
        String text = relativePos.toShortString();
        this.markers.put(absolutePos, new Marker(0x6000FF00, text, Util.getMillis() + 10000L));
    }

    public void clear() {
        this.markers.clear();
    }

    public void emitGizmos() {
        long time = Util.getMillis();
        this.markers.entrySet().removeIf(entry -> time > ((Marker)entry.getValue()).removeAtTime);
        this.markers.forEach((pos, marker) -> this.renderMarker((BlockPos)pos, (Marker)marker));
    }

    private void renderMarker(BlockPos pos, Marker marker) {
        Gizmos.cuboid(pos, 0.02f, GizmoStyle.fill(marker.color()));
        if (!marker.text.isEmpty()) {
            Gizmos.billboardText(marker.text, Vec3.atLowerCornerWithOffset(pos, 0.5, 1.2, 0.5), TextGizmo.Style.whiteAndCentered().withScale(0.16f)).setAlwaysOnTop();
        }
    }

    private record Marker(int color, String text, long removeAtTime) {
    }
}

