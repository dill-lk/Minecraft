/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.debug;

import java.util.HashMap;
import java.util.Map;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.debug.DebugRenderer;
import net.mayaan.core.BlockPos;
import net.mayaan.gizmos.GizmoStyle;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.gizmos.TextGizmo;
import net.mayaan.util.debug.DebugSubscriptions;
import net.mayaan.util.debug.DebugValueAccess;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.Vec3;

public class NeighborsUpdateRenderer
implements DebugRenderer.SimpleDebugRenderer {
    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        LastUpdate lastUpdate;
        BlockPos pos;
        int shrinkTime = DebugSubscriptions.NEIGHBOR_UPDATES.expireAfterTicks();
        double shrinkSpeed = 1.0 / (double)(shrinkTime * 2);
        HashMap lastUpdates = new HashMap();
        debugValues.forEachEvent(DebugSubscriptions.NEIGHBOR_UPDATES, (blockPos, remainingTicks, totalLifetime) -> {
            long age = totalLifetime - remainingTicks;
            LastUpdate lastUpdate = lastUpdates.getOrDefault(blockPos, LastUpdate.NONE);
            lastUpdates.put(blockPos, lastUpdate.tryCount((int)age));
        });
        for (Map.Entry entry : lastUpdates.entrySet()) {
            pos = (BlockPos)entry.getKey();
            lastUpdate = (LastUpdate)entry.getValue();
            AABB aabb = new AABB(pos).inflate(0.002).deflate(shrinkSpeed * (double)lastUpdate.age);
            Gizmos.cuboid(aabb, GizmoStyle.stroke(-1));
        }
        for (Map.Entry entry : lastUpdates.entrySet()) {
            pos = (BlockPos)entry.getKey();
            lastUpdate = (LastUpdate)entry.getValue();
            Gizmos.billboardText(String.valueOf(lastUpdate.count), Vec3.atCenterOf(pos), TextGizmo.Style.whiteAndCentered());
        }
    }

    private record LastUpdate(int count, int age) {
        private static final LastUpdate NONE = new LastUpdate(0, Integer.MAX_VALUE);

        public LastUpdate tryCount(int age) {
            if (age == this.age) {
                return new LastUpdate(this.count + 1, age);
            }
            if (age < this.age) {
                return new LastUpdate(1, age);
            }
            return this;
        }
    }
}

