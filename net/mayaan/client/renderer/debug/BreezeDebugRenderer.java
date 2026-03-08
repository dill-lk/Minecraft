/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.debug;

import net.mayaan.client.Mayaan;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.debug.DebugRenderer;
import net.mayaan.gizmos.GizmoStyle;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.util.ARGB;
import net.mayaan.util.debug.DebugSubscriptions;
import net.mayaan.util.debug.DebugValueAccess;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.Vec3;

public class BreezeDebugRenderer
implements DebugRenderer.SimpleDebugRenderer {
    private static final int JUMP_TARGET_LINE_COLOR = ARGB.color(255, 255, 100, 255);
    private static final int TARGET_LINE_COLOR = ARGB.color(255, 100, 255, 255);
    private static final int INNER_CIRCLE_COLOR = ARGB.color(255, 0, 255, 0);
    private static final int MIDDLE_CIRCLE_COLOR = ARGB.color(255, 255, 165, 0);
    private static final int OUTER_CIRCLE_COLOR = ARGB.color(255, 255, 0, 0);
    private final Mayaan minecraft;

    public BreezeDebugRenderer(Mayaan minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        ClientLevel level = this.minecraft.level;
        debugValues.forEachEntity(DebugSubscriptions.BREEZES, (entity, info) -> {
            info.attackTarget().map(level::getEntity).map(targetEntity -> targetEntity.getPosition(this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true))).ifPresent(attackTargetPosition -> {
                Gizmos.arrow(entity.position(), attackTargetPosition, TARGET_LINE_COLOR);
                Vec3 drawCenter = attackTargetPosition.add(0.0, 0.01f, 0.0);
                Gizmos.circle(drawCenter, 4.0f, GizmoStyle.stroke(INNER_CIRCLE_COLOR));
                Gizmos.circle(drawCenter, 8.0f, GizmoStyle.stroke(MIDDLE_CIRCLE_COLOR));
                Gizmos.circle(drawCenter, 24.0f, GizmoStyle.stroke(OUTER_CIRCLE_COLOR));
            });
            info.jumpTarget().ifPresent(blockPos -> {
                Gizmos.arrow(entity.position(), blockPos.getCenter(), JUMP_TARGET_LINE_COLOR);
                Gizmos.cuboid(AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(blockPos)), GizmoStyle.fill(ARGB.colorFromFloat(1.0f, 1.0f, 0.0f, 0.0f)));
            });
        });
    }
}

