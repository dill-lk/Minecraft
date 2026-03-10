/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.debug;

import net.mayaan.client.Camera;
import net.mayaan.client.Mayaan;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.debug.DebugRenderer;
import net.mayaan.core.BlockPos;
import net.mayaan.gizmos.GizmoStyle;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.gizmos.TextGizmo;
import net.mayaan.util.ARGB;
import net.mayaan.util.debug.DebugSubscriptions;
import net.mayaan.util.debug.DebugValueAccess;
import net.mayaan.world.phys.Vec3;

public class RaidDebugRenderer
implements DebugRenderer.SimpleDebugRenderer {
    private static final int MAX_RENDER_DIST = 160;
    private static final float TEXT_SCALE = 0.64f;
    private final Mayaan minecraft;

    public RaidDebugRenderer(Mayaan minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        BlockPos playerPos = this.getCamera().blockPosition();
        debugValues.forEachChunk(DebugSubscriptions.RAIDS, (chunkPos, raidCenters) -> {
            for (BlockPos raidCenter : raidCenters) {
                if (!playerPos.closerThan(raidCenter, 160.0)) continue;
                RaidDebugRenderer.highlightRaidCenter(raidCenter);
            }
        });
    }

    private static void highlightRaidCenter(BlockPos raidCenter) {
        Gizmos.cuboid(raidCenter, GizmoStyle.fill(ARGB.colorFromFloat(0.15f, 1.0f, 0.0f, 0.0f)));
        RaidDebugRenderer.renderTextOverBlock("Raid center", raidCenter, -65536);
    }

    private static void renderTextOverBlock(String text, BlockPos pos, int color) {
        Gizmos.billboardText(text, Vec3.atLowerCornerWithOffset(pos, 0.5, 1.3, 0.5), TextGizmo.Style.forColor(color).withScale(0.64f)).setAlwaysOnTop();
    }

    private Camera getCamera() {
        return this.minecraft.gameRenderer.getMainCamera();
    }
}

