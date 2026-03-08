/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components;

import net.mayaan.client.gui.ComponentPath;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.narration.NarrationElementOutput;
import net.mayaan.client.gui.navigation.FocusNavigationEvent;
import net.mayaan.client.gui.screens.LoadingDotsText;
import net.mayaan.client.sounds.SoundManager;
import net.mayaan.network.chat.Component;
import net.mayaan.util.Util;
import org.jspecify.annotations.Nullable;

public class LoadingDotsWidget
extends AbstractWidget {
    private final Font font;

    public LoadingDotsWidget(Font font, Component message) {
        super(0, 0, font.width(message), font.lineHeight * 3, message);
        this.font = font;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        int centerX = this.getX() + this.getWidth() / 2;
        int centerY = this.getY() + this.getHeight() / 2;
        Component message = this.getMessage();
        graphics.drawString(this.font, message, centerX - this.font.width(message) / 2, centerY - this.font.lineHeight, -1);
        String dots = LoadingDotsText.get(Util.getMillis());
        graphics.drawString(this.font, dots, centerX - this.font.width(dots) / 2, centerY + this.font.lineHeight, -8355712);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
        return null;
    }
}

