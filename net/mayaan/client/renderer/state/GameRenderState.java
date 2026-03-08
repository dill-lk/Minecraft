/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.state;

import net.mayaan.client.renderer.state.LightmapRenderState;
import net.mayaan.client.renderer.state.OptionsRenderState;
import net.mayaan.client.renderer.state.WindowRenderState;
import net.mayaan.client.renderer.state.gui.GuiRenderState;
import net.mayaan.client.renderer.state.level.LevelRenderState;

public class GameRenderState {
    public final LevelRenderState levelRenderState = new LevelRenderState();
    public final LightmapRenderState lightmapRenderState = new LightmapRenderState();
    public final GuiRenderState guiRenderState = new GuiRenderState();
    public final OptionsRenderState optionsRenderState = new OptionsRenderState();
    public final WindowRenderState windowRenderState = new WindowRenderState();
    public int framerateLimit;
}

