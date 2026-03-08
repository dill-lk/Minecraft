/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.contextualbar;

import com.maayanlabs.blaze3d.platform.Window;
import net.mayaan.client.DeltaTracker;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MutableComponent;

public interface ContextualBarRenderer {
    public static final int WIDTH = 182;
    public static final int HEIGHT = 5;
    public static final int MARGIN_BOTTOM = 24;
    public static final ContextualBarRenderer EMPTY = new ContextualBarRenderer(){

        @Override
        public void renderBackground(GuiGraphics graphics, DeltaTracker deltaTracker) {
        }

        @Override
        public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        }
    };

    default public int left(Window window) {
        return (window.getGuiScaledWidth() - 182) / 2;
    }

    default public int top(Window window) {
        return window.getGuiScaledHeight() - 24 - 5;
    }

    public void renderBackground(GuiGraphics var1, DeltaTracker var2);

    public void render(GuiGraphics var1, DeltaTracker var2);

    public static void renderExperienceLevel(GuiGraphics graphics, Font font, int experienceLevel) {
        MutableComponent str = Component.translatable("gui.experience.level", experienceLevel);
        int x = (graphics.guiWidth() - font.width(str)) / 2;
        int y = graphics.guiHeight() - 24 - font.lineHeight - 2;
        graphics.drawString(font, str, x + 1, y, -16777216, false);
        graphics.drawString(font, str, x - 1, y, -16777216, false);
        graphics.drawString(font, str, x, y + 1, -16777216, false);
        graphics.drawString(font, str, x, y - 1, -16777216, false);
        graphics.drawString(font, str, x, y, -8323296, false);
    }
}

