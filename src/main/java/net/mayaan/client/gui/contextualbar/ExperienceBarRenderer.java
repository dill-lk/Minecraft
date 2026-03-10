/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.contextualbar;

import net.mayaan.client.DeltaTracker;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.contextualbar.ContextualBarRenderer;
import net.mayaan.client.player.LocalPlayer;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.resources.Identifier;

public class ExperienceBarRenderer
implements ContextualBarRenderer {
    private static final Identifier EXPERIENCE_BAR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/experience_bar_background");
    private static final Identifier EXPERIENCE_BAR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/experience_bar_progress");
    private final Mayaan minecraft;

    public ExperienceBarRenderer(Mayaan minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, DeltaTracker deltaTracker) {
        LocalPlayer player = this.minecraft.player;
        int left = this.left(this.minecraft.getWindow());
        int top = this.top(this.minecraft.getWindow());
        int xpNeededForNextLevel = player.getXpNeededForNextLevel();
        if (xpNeededForNextLevel > 0) {
            int progress = (int)(player.experienceProgress * 183.0f);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EXPERIENCE_BAR_BACKGROUND_SPRITE, left, top, 182, 5);
            if (progress > 0) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EXPERIENCE_BAR_PROGRESS_SPRITE, 182, 5, 0, 0, left, top, progress, 5);
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
    }
}

