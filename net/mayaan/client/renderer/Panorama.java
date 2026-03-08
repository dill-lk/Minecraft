/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer;

import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.client.renderer.state.gui.PanoramaRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Mth;

public class Panorama {
    public static final Identifier PANORAMA_OVERLAY = Identifier.withDefaultNamespace("textures/gui/title/background/panorama_overlay.png");
    private float spin;

    public void render(GuiGraphics graphics, int width, int height, boolean shouldSpin) {
        Mayaan minecraft = Mayaan.getInstance();
        if (shouldSpin) {
            float a = minecraft.getDeltaTracker().getRealtimeDeltaTicks();
            float delta = (float)((double)a * minecraft.gameRenderer.getGameRenderState().optionsRenderState.panoramaSpeed);
            this.spin = Mth.wrapDegrees(this.spin + delta * 0.1f);
        }
        minecraft.gameRenderer.getGameRenderState().guiRenderState.panoramaRenderState = new PanoramaRenderState(-this.spin);
        graphics.blit(RenderPipelines.GUI_TEXTURED, PANORAMA_OVERLAY, 0, 0, 0.0f, 0.0f, width, height, 16, 128, 16, 128);
    }
}

