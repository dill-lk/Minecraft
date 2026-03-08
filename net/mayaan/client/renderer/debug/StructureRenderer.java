/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.debug;

import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.debug.DebugRenderer;
import net.mayaan.gizmos.GizmoStyle;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.util.ARGB;
import net.mayaan.util.debug.DebugStructureInfo;
import net.mayaan.util.debug.DebugSubscriptions;
import net.mayaan.util.debug.DebugValueAccess;
import net.mayaan.world.phys.AABB;

public class StructureRenderer
implements DebugRenderer.SimpleDebugRenderer {
    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        debugValues.forEachChunk(DebugSubscriptions.STRUCTURES, (chunkPos, structures) -> {
            for (DebugStructureInfo structure : structures) {
                Gizmos.cuboid(AABB.of(structure.boundingBox()), GizmoStyle.stroke(ARGB.colorFromFloat(1.0f, 1.0f, 1.0f, 1.0f)));
                for (DebugStructureInfo.Piece piece : structure.pieces()) {
                    if (piece.isStart()) {
                        Gizmos.cuboid(AABB.of(piece.boundingBox()), GizmoStyle.stroke(ARGB.colorFromFloat(1.0f, 0.0f, 1.0f, 0.0f)));
                        continue;
                    }
                    Gizmos.cuboid(AABB.of(piece.boundingBox()), GizmoStyle.stroke(ARGB.colorFromFloat(1.0f, 0.0f, 0.0f, 1.0f)));
                }
            }
        });
    }
}

