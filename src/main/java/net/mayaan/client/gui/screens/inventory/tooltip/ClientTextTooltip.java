/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.inventory.tooltip;

import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.mayaan.util.FormattedCharSequence;

public class ClientTextTooltip
implements ClientTooltipComponent {
    private final FormattedCharSequence text;

    public ClientTextTooltip(FormattedCharSequence text) {
        this.text = text;
    }

    @Override
    public int getWidth(Font font) {
        return font.width(this.text);
    }

    @Override
    public int getHeight(Font font) {
        return 10;
    }

    @Override
    public void renderText(GuiGraphics guiGraphics, Font font, int x, int y) {
        guiGraphics.drawString(font, this.text, x, y, -1, true);
    }
}

