/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.debug;

import net.mayaan.client.Mayaan;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.debug.DebugRenderer;
import net.mayaan.core.BlockPos;
import net.mayaan.gizmos.GizmoStyle;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.gizmos.TextGizmo;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.ARGB;
import net.mayaan.util.debug.DebugValueAccess;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.Vec3;

public class WaterDebugRenderer
implements DebugRenderer.SimpleDebugRenderer {
    private final Mayaan minecraft;

    public WaterDebugRenderer(Mayaan minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        FluidState fluidState;
        BlockPos pos = this.minecraft.player.blockPosition();
        Level level = this.minecraft.player.level();
        for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-10, -10, -10), pos.offset(10, 10, 10))) {
            fluidState = level.getFluidState(blockPos);
            if (!fluidState.is(FluidTags.WATER)) continue;
            double height = (float)blockPos.getY() + fluidState.getHeight(level, blockPos);
            Gizmos.cuboid(new AABB((float)blockPos.getX() + 0.01f, (float)blockPos.getY() + 0.01f, (float)blockPos.getZ() + 0.01f, (float)blockPos.getX() + 0.99f, height, (float)blockPos.getZ() + 0.99f), GizmoStyle.fill(ARGB.colorFromFloat(0.15f, 0.0f, 1.0f, 0.0f)));
        }
        for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-10, -10, -10), pos.offset(10, 10, 10))) {
            fluidState = level.getFluidState(blockPos);
            if (!fluidState.is(FluidTags.WATER)) continue;
            Gizmos.billboardText(String.valueOf(fluidState.getAmount()), Vec3.atLowerCornerWithOffset(blockPos, 0.5, fluidState.getHeight(level, blockPos), 0.5), TextGizmo.Style.forColorAndCentered(-16777216));
        }
    }
}

