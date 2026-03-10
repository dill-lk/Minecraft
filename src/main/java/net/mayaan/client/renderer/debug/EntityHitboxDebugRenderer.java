/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.debug;

import net.mayaan.SharedConstants;
import net.mayaan.client.CameraType;
import net.mayaan.client.Mayaan;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.debug.DebugRenderer;
import net.mayaan.client.server.IntegratedServer;
import net.mayaan.gizmos.GizmoStyle;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.gizmos.TextGizmo;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.ARGB;
import net.mayaan.util.debug.DebugValueAccess;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.boss.enderdragon.EnderDragon;
import net.mayaan.world.entity.boss.enderdragon.EnderDragonPart;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class EntityHitboxDebugRenderer
implements DebugRenderer.SimpleDebugRenderer {
    final Mayaan minecraft;

    public EntityHitboxDebugRenderer(Mayaan minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        if (this.minecraft.level == null) {
            return;
        }
        for (Entity entity : this.minecraft.level.entitiesForRendering()) {
            if (entity.isInvisible() || !frustum.isVisible(entity.getBoundingBox()) || entity == this.minecraft.getCameraEntity() && this.minecraft.options.getCameraType() == CameraType.FIRST_PERSON) continue;
            float entityPartialTicks = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(!this.minecraft.level.tickRateManager().isEntityFrozen(entity));
            this.showHitboxes(entity, entityPartialTicks, false);
            if (!SharedConstants.DEBUG_SHOW_LOCAL_SERVER_ENTITY_HIT_BOXES) continue;
            Entity serverEntity = this.getServerEntity(entity);
            if (serverEntity != null) {
                this.showHitboxes(entity, entityPartialTicks, true);
                continue;
            }
            Gizmos.billboardText("Missing Server Entity", entity.getPosition(entityPartialTicks).add(0.0, entity.getBoundingBox().getYsize() + 1.5, 0.0), TextGizmo.Style.forColorAndCentered(-65536));
        }
    }

    private @Nullable Entity getServerEntity(Entity entity) {
        ServerLevel level;
        IntegratedServer server = this.minecraft.getSingleplayerServer();
        if (server != null && (level = server.getLevel(entity.level().dimension())) != null) {
            return level.getEntity(entity.getId());
        }
        return null;
    }

    private void showHitboxes(Entity entity, float partialTicks, boolean isServerEntity) {
        Vec3 latestPosition = entity.position();
        Vec3 currentPosition = entity.getPosition(partialTicks);
        Vec3 offset = currentPosition.subtract(latestPosition);
        int mainColor = isServerEntity ? -16711936 : -1;
        Gizmos.cuboid(entity.getBoundingBox().move(offset), GizmoStyle.stroke(mainColor));
        Gizmos.point(currentPosition, mainColor, 2.0f);
        Entity vehicle = entity.getVehicle();
        if (vehicle != null) {
            float width = Math.min(vehicle.getBbWidth(), entity.getBbWidth()) / 2.0f;
            float height = 0.0625f;
            Vec3 position = vehicle.getPassengerRidingPosition(entity).add(offset);
            Gizmos.cuboid(new AABB(position.x - (double)width, position.y, position.z - (double)width, position.x + (double)width, position.y + 0.0625, position.z + (double)width), GizmoStyle.stroke(-256));
        }
        if (entity instanceof LivingEntity) {
            AABB bb = entity.getBoundingBox().move(offset);
            float padding = 0.01f;
            Gizmos.cuboid(new AABB(bb.minX, bb.minY + (double)entity.getEyeHeight() - (double)0.01f, bb.minZ, bb.maxX, bb.minY + (double)entity.getEyeHeight() + (double)0.01f, bb.maxZ), GizmoStyle.stroke(-65536));
        }
        if (entity instanceof EnderDragon) {
            EnderDragon dragon = (EnderDragon)entity;
            for (EnderDragonPart subEntity : dragon.getSubEntities()) {
                Vec3 latestSubPosition = subEntity.position();
                Vec3 currentSubPosition = subEntity.getPosition(partialTicks);
                Vec3 subOffset = currentSubPosition.subtract(latestSubPosition);
                Gizmos.cuboid(subEntity.getBoundingBox().move(subOffset), GizmoStyle.stroke(ARGB.colorFromFloat(1.0f, 0.25f, 1.0f, 0.0f)));
            }
        }
        Vec3 eyePosition = currentPosition.add(0.0, entity.getEyeHeight(), 0.0);
        Vec3 viewVector = entity.getViewVector(partialTicks);
        Gizmos.arrow(eyePosition, eyePosition.add(viewVector.scale(2.0)), -16776961);
        if (isServerEntity) {
            Vec3 deltaMovement = entity.getDeltaMovement();
            Gizmos.arrow(currentPosition, currentPosition.add(deltaMovement), -256);
        }
    }
}

