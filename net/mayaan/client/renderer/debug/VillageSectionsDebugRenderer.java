/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.debug;

import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.debug.DebugRenderer;
import net.mayaan.core.SectionPos;
import net.mayaan.gizmos.GizmoStyle;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.util.ARGB;
import net.mayaan.util.debug.DebugSubscriptions;
import net.mayaan.util.debug.DebugValueAccess;

public class VillageSectionsDebugRenderer
implements DebugRenderer.SimpleDebugRenderer {
    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        debugValues.forEachBlock(DebugSubscriptions.VILLAGE_SECTIONS, (pos, ignored) -> {
            SectionPos villageSection = SectionPos.of(pos);
            Gizmos.cuboid(villageSection.center(), GizmoStyle.fill(ARGB.colorFromFloat(0.15f, 0.2f, 1.0f, 0.2f)));
        });
    }
}

