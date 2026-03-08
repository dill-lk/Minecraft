/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.components;

import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.AbstractScrollArea;
import net.mayaan.client.gui.components.AbstractTextAreaWidget;
import net.mayaan.client.gui.components.MultiLineTextWidget;
import net.mayaan.client.gui.narration.NarratedElementType;
import net.mayaan.client.gui.narration.NarrationElementOutput;
import net.mayaan.network.chat.Component;

public class FittingMultiLineTextWidget
extends AbstractTextAreaWidget {
    private final MultiLineTextWidget multilineWidget;

    public FittingMultiLineTextWidget(int x, int y, int width, int height, Component message, Font font) {
        super(x, y, width, height, message, AbstractScrollArea.defaultSettings(font.lineHeight));
        this.multilineWidget = new MultiLineTextWidget(message, font).setMaxWidth(this.getWidth() - this.totalInnerPadding());
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        this.multilineWidget.setMaxWidth(this.getWidth() - this.totalInnerPadding());
    }

    @Override
    protected int getInnerHeight() {
        return this.multilineWidget.getHeight();
    }

    public void minimizeHeight() {
        if (!this.showingScrollBar()) {
            this.setHeight(this.getInnerHeight() + this.totalInnerPadding());
        }
    }

    @Override
    protected void renderBackground(GuiGraphics graphics) {
        super.renderBackground(graphics);
    }

    public boolean showingScrollBar() {
        return super.scrollable();
    }

    @Override
    protected void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        graphics.pose().pushMatrix();
        graphics.pose().translate((float)this.getInnerLeft(), (float)this.getInnerTop());
        this.multilineWidget.render(graphics, mouseX, mouseY, a);
        graphics.pose().popMatrix();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, this.getMessage());
    }

    @Override
    public void setMessage(Component message) {
        super.setMessage(message);
        this.multilineWidget.setMessage(message);
    }
}

