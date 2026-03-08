/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.state.level;

import java.util.ArrayList;
import java.util.List;
import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.client.renderer.chunk.ChunkSectionsToRender;
import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.client.renderer.state.level.BlockBreakingRenderState;
import net.mayaan.client.renderer.state.level.BlockOutlineRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.state.level.ParticlesRenderState;
import net.mayaan.client.renderer.state.level.SkyRenderState;
import net.mayaan.client.renderer.state.level.WeatherRenderState;
import net.mayaan.client.renderer.state.level.WorldBorderRenderState;
import org.jspecify.annotations.Nullable;

public class LevelRenderState {
    public CameraRenderState cameraRenderState = new CameraRenderState();
    public final List<EntityRenderState> entityRenderStates = new ArrayList<EntityRenderState>();
    public final List<BlockEntityRenderState> blockEntityRenderStates = new ArrayList<BlockEntityRenderState>();
    public boolean haveGlowingEntities;
    public @Nullable BlockOutlineRenderState blockOutlineRenderState;
    public final List<BlockBreakingRenderState> blockBreakingRenderStates = new ArrayList<BlockBreakingRenderState>();
    public final WeatherRenderState weatherRenderState = new WeatherRenderState();
    public final WorldBorderRenderState worldBorderRenderState = new WorldBorderRenderState();
    public final SkyRenderState skyRenderState = new SkyRenderState();
    public final ParticlesRenderState particlesRenderState = new ParticlesRenderState();
    public long gameTime;
    public int lastEntityRenderStateCount;
    public int cloudColor;
    public float cloudHeight;
    public @Nullable ChunkSectionsToRender chunkSectionsToRender;

    public void reset() {
        this.entityRenderStates.clear();
        this.blockEntityRenderStates.clear();
        this.blockBreakingRenderStates.clear();
        this.haveGlowingEntities = false;
        this.blockOutlineRenderState = null;
        this.weatherRenderState.reset();
        this.worldBorderRenderState.reset();
        this.skyRenderState.reset();
        this.gameTime = 0L;
    }
}

