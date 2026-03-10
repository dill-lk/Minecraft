/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 */
package net.mayaan.client.renderer.debug;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import net.mayaan.client.Mayaan;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.debug.DebugRenderer;
import net.mayaan.gizmos.GizmoStyle;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.util.Util;
import net.mayaan.util.debug.DebugValueAccess;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.shapes.VoxelShape;

public class CollisionBoxRenderer
implements DebugRenderer.SimpleDebugRenderer {
    private final Mayaan minecraft;
    private double lastUpdateTime = Double.MIN_VALUE;
    private List<VoxelShape> shapes = Collections.emptyList();

    public CollisionBoxRenderer(Mayaan minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        double time = Util.getNanos();
        if (time - this.lastUpdateTime > 1.0E8) {
            this.lastUpdateTime = time;
            Entity cameraEntity = this.minecraft.getCameraEntity();
            this.shapes = ImmutableList.copyOf(cameraEntity.level().getCollisions(cameraEntity, cameraEntity.getBoundingBox().inflate(6.0)));
        }
        for (VoxelShape shape : this.shapes) {
            GizmoStyle style = GizmoStyle.stroke(-1);
            for (AABB aabb : shape.toAabbs()) {
                Gizmos.cuboid(aabb, style);
            }
        }
    }
}

