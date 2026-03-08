/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.debug;

import net.mayaan.client.Camera;
import net.mayaan.client.Mayaan;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.debug.DebugRenderer;
import net.mayaan.core.BlockPos;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.gizmos.TextGizmo;
import net.mayaan.util.debug.DebugGoalInfo;
import net.mayaan.util.debug.DebugSubscriptions;
import net.mayaan.util.debug.DebugValueAccess;
import net.mayaan.world.phys.Vec3;

public class GoalSelectorDebugRenderer
implements DebugRenderer.SimpleDebugRenderer {
    private static final int MAX_RENDER_DIST = 160;
    private final Mayaan minecraft;

    public GoalSelectorDebugRenderer(Mayaan minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        Camera camera = this.minecraft.gameRenderer.getMainCamera();
        BlockPos playerPos = BlockPos.containing(camera.position().x, 0.0, camera.position().z);
        debugValues.forEachEntity(DebugSubscriptions.GOAL_SELECTORS, (entity, goalInfo) -> {
            if (playerPos.closerThan(entity.blockPosition(), 160.0)) {
                for (int i = 0; i < goalInfo.goals().size(); ++i) {
                    DebugGoalInfo.DebugGoal goal = goalInfo.goals().get(i);
                    double x = (double)entity.getBlockX() + 0.5;
                    double y = entity.getY() + 2.0 + (double)i * 0.25;
                    double z = (double)entity.getBlockZ() + 0.5;
                    int color = goal.isRunning() ? -16711936 : -3355444;
                    Gizmos.billboardText(goal.name(), new Vec3(x, y, z), TextGizmo.Style.forColorAndCentered(color));
                }
            }
        });
    }
}

