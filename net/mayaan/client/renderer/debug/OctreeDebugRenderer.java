/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.mutable.MutableInt
 */
package net.mayaan.client.renderer.debug;

import net.mayaan.client.Mayaan;
import net.mayaan.client.renderer.Octree;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.debug.DebugRenderer;
import net.mayaan.gizmos.GizmoStyle;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.gizmos.TextGizmo;
import net.mayaan.util.ARGB;
import net.mayaan.util.Mth;
import net.mayaan.util.debug.DebugValueAccess;
import net.mayaan.world.phys.AABB;
import org.apache.commons.lang3.mutable.MutableInt;

public class OctreeDebugRenderer
implements DebugRenderer.SimpleDebugRenderer {
    private final Mayaan minecraft;

    public OctreeDebugRenderer(Mayaan minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        Octree octree = this.minecraft.levelRenderer.getSectionOcclusionGraph().getOctree();
        MutableInt count = new MutableInt(0);
        octree.visitNodes((node, fullyVisible, depth, isClose) -> this.renderNode(node, depth, fullyVisible, count, isClose), frustum, 32);
    }

    private void renderNode(Octree.Node node, int depth, boolean fullyVisible, MutableInt count, boolean isClose) {
        AABB aabb = node.getAABB();
        double xSize = aabb.getXsize();
        long size = Math.round(xSize / 16.0);
        if (size == 1L) {
            count.add(1);
            int color = isClose ? -16711936 : -1;
            Gizmos.billboardText(String.valueOf(count.intValue()), aabb.getCenter(), TextGizmo.Style.forColorAndCentered(color).withScale(4.8f));
        }
        long colorNum = size + 5L;
        Gizmos.cuboid(aabb.deflate(0.1 * (double)depth), GizmoStyle.stroke(ARGB.colorFromFloat(fullyVisible ? 0.4f : 1.0f, OctreeDebugRenderer.getColorComponent(colorNum, 0.3f), OctreeDebugRenderer.getColorComponent(colorNum, 0.8f), OctreeDebugRenderer.getColorComponent(colorNum, 0.5f))));
    }

    private static float getColorComponent(long size, float multiplier) {
        float minColor = 0.1f;
        return Mth.frac(multiplier * (float)size) * 0.9f + 0.1f;
    }
}

