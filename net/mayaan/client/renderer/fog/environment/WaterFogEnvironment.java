/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.fog.environment;

import net.mayaan.client.Camera;
import net.mayaan.client.DeltaTracker;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.player.LocalPlayer;
import net.mayaan.client.renderer.fog.FogData;
import net.mayaan.client.renderer.fog.environment.FogEnvironment;
import net.mayaan.world.attribute.EnvironmentAttributes;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.material.FogType;
import org.jspecify.annotations.Nullable;

public class WaterFogEnvironment
extends FogEnvironment {
    @Override
    public void setupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
        float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
        fog.environmentalStart = camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_START_DISTANCE, partialTicks).floatValue();
        fog.environmentalEnd = camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_END_DISTANCE, partialTicks).floatValue();
        Entity entity = camera.entity();
        if (entity instanceof LocalPlayer) {
            LocalPlayer player = (LocalPlayer)entity;
            fog.environmentalEnd *= Math.max(0.25f, player.getWaterVision());
        }
        fog.skyEnd = fog.environmentalEnd;
        fog.cloudEnd = fog.environmentalEnd;
    }

    @Override
    public boolean isApplicable(@Nullable FogType fogType, Entity entity) {
        return fogType == FogType.WATER;
    }

    @Override
    public int getBaseColor(ClientLevel level, Camera camera, int renderDistance, float partialTicks) {
        return camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_COLOR, partialTicks);
    }
}

