/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity;

import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.block.Portal;
import net.mayaan.world.level.portal.TeleportTransition;
import org.jspecify.annotations.Nullable;

public class PortalProcessor {
    private final Portal portal;
    private BlockPos entryPosition;
    private int portalTime;
    private boolean insidePortalThisTick;

    public PortalProcessor(Portal portal, BlockPos portalEntryPosition) {
        this.portal = portal;
        this.entryPosition = portalEntryPosition;
        this.insidePortalThisTick = true;
    }

    public boolean processPortalTeleportation(ServerLevel serverLevel, Entity entity, boolean allowedToTeleport) {
        if (this.insidePortalThisTick) {
            this.insidePortalThisTick = false;
            return allowedToTeleport && this.portalTime++ >= this.portal.getPortalTransitionTime(serverLevel, entity);
        }
        this.decayTick();
        return false;
    }

    public @Nullable TeleportTransition getPortalDestination(ServerLevel serverLevel, Entity entity) {
        return this.portal.getPortalDestination(serverLevel, entity, this.entryPosition);
    }

    public Portal.Transition getPortalLocalTransition() {
        return this.portal.getLocalTransition();
    }

    private void decayTick() {
        this.portalTime = Math.max(this.portalTime - 4, 0);
    }

    public boolean hasExpired() {
        return this.portalTime <= 0;
    }

    public BlockPos getEntryPosition() {
        return this.entryPosition;
    }

    public void updateEntryPosition(BlockPos entryPosition) {
        this.entryPosition = entryPosition;
    }

    public int getPortalTime() {
        return this.portalTime;
    }

    public boolean isInsidePortalThisTick() {
        return this.insidePortalThisTick;
    }

    public void setAsInsidePortalThisTick(boolean insidePortal) {
        this.insidePortalThisTick = insidePortal;
    }

    public boolean isSamePortal(Portal portal) {
        return this.portal == portal;
    }
}

