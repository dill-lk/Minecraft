/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components;

import java.time.Duration;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.Tooltip;
import net.mayaan.client.gui.narration.NarrationElementOutput;
import net.mayaan.client.gui.navigation.ScreenRectangle;
import net.mayaan.client.gui.screens.inventory.tooltip.BelowOrAboveWidgetTooltipPositioner;
import net.mayaan.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.mayaan.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.mayaan.util.Util;
import org.jspecify.annotations.Nullable;

public class WidgetTooltipHolder {
    private @Nullable Tooltip tooltip;
    private Duration delay = Duration.ZERO;
    private long displayStartTime;
    private boolean wasDisplayed;

    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    public void set(@Nullable Tooltip tooltip) {
        this.tooltip = tooltip;
    }

    public @Nullable Tooltip get() {
        return this.tooltip;
    }

    public void refreshTooltipForNextRenderPass(GuiGraphics graphics, int mouseX, int mouseY, boolean isHovered, boolean isFocused, ScreenRectangle screenRectangle) {
        boolean shouldDisplay;
        if (this.tooltip == null) {
            this.wasDisplayed = false;
            return;
        }
        Mayaan minecraft = Mayaan.getInstance();
        boolean bl = shouldDisplay = isHovered || isFocused && minecraft.getLastInputType().isKeyboard();
        if (shouldDisplay != this.wasDisplayed) {
            if (shouldDisplay) {
                this.displayStartTime = Util.getMillis();
            }
            this.wasDisplayed = shouldDisplay;
        }
        if (shouldDisplay && Util.getMillis() - this.displayStartTime > this.delay.toMillis()) {
            graphics.setTooltipForNextFrame(minecraft.font, this.tooltip.toCharSequence(minecraft), this.tooltip.component(), this.createTooltipPositioner(screenRectangle, isHovered, isFocused), mouseX, mouseY, isFocused, this.tooltip.style());
        }
    }

    private ClientTooltipPositioner createTooltipPositioner(ScreenRectangle screenRectangle, boolean isHovered, boolean isFocused) {
        if (!isHovered && isFocused && Mayaan.getInstance().getLastInputType().isKeyboard()) {
            return new BelowOrAboveWidgetTooltipPositioner(screenRectangle);
        }
        return new MenuTooltipPositioner(screenRectangle);
    }

    public void updateNarration(NarrationElementOutput output) {
        if (this.tooltip != null) {
            this.tooltip.updateNarration(output);
        }
    }
}

