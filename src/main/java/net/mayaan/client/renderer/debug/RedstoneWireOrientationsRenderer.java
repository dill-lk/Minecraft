/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.debug;

import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.debug.DebugRenderer;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.util.debug.DebugSubscriptions;
import net.mayaan.util.debug.DebugValueAccess;
import net.mayaan.world.phys.Vec3;

public class RedstoneWireOrientationsRenderer
implements DebugRenderer.SimpleDebugRenderer {
    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        debugValues.forEachBlock(DebugSubscriptions.REDSTONE_WIRE_ORIENTATIONS, (wirePos, orientation) -> {
            Vec3 center = wirePos.getBottomCenter().subtract(0.0, 0.1, 0.0);
            Gizmos.arrow(center, center.add(orientation.getFront().getUnitVec3().scale(0.5)), -16776961);
            Gizmos.arrow(center, center.add(orientation.getUp().getUnitVec3().scale(0.4)), -65536);
            Gizmos.arrow(center, center.add(orientation.getSide().getUnitVec3().scale(0.3)), -256);
        });
    }
}

