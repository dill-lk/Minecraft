/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.components;

import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.Button;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentUtils;
import net.mayaan.network.chat.Style;
import net.mayaan.util.Mth;

public class PlainTextButton
extends Button {
    private final Font font;
    private final Component message;
    private final Component underlinedMessage;

    public PlainTextButton(int x, int y, int width, int height, Component message, Button.OnPress onPress, Font font) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.font = font;
        this.message = message;
        this.underlinedMessage = ComponentUtils.mergeStyles(message, Style.EMPTY.withUnderlined(true));
    }

    @Override
    public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        Component messageToRender = this.isHoveredOrFocused() ? this.underlinedMessage : this.message;
        graphics.drawString(this.font, messageToRender, this.getX(), this.getY(), 0xFFFFFF | Mth.ceil(this.alpha * 255.0f) << 24);
    }
}

