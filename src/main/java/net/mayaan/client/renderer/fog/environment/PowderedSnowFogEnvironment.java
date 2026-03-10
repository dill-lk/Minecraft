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
import net.mayaan.client.renderer.fog.FogData;
import net.mayaan.client.renderer.fog.environment.FogEnvironment;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.material.FogType;
import org.jspecify.annotations.Nullable;

public class PowderedSnowFogEnvironment
extends FogEnvironment {
    private static final int COLOR = -6308916;

    @Override
    public int getBaseColor(ClientLevel level, Camera camera, int renderDistance, float partialTicks) {
        return -6308916;
    }

    @Override
    public void setupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
        if (camera.entity().isSpectator()) {
            fog.environmentalStart = -8.0f;
            fog.environmentalEnd = renderDistance * 0.5f;
        } else {
            fog.environmentalStart = 0.0f;
            fog.environmentalEnd = 2.0f;
        }
        fog.skyEnd = fog.environmentalEnd;
        fog.cloudEnd = fog.environmentalEnd;
    }

    @Override
    public boolean isApplicable(@Nullable FogType fogType, Entity entity) {
        return fogType == FogType.POWDER_SNOW;
    }
}

