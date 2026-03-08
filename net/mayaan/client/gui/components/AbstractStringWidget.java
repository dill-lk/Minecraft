/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components;

import java.util.function.Consumer;
import net.mayaan.client.gui.ActiveTextCollector;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.narration.NarrationElementOutput;
import net.mayaan.client.input.MouseButtonEvent;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.Style;
import org.jspecify.annotations.Nullable;

public abstract class AbstractStringWidget
extends AbstractWidget {
    private @Nullable Consumer<Style> componentClickHandler = null;
    private final Font font;

    public AbstractStringWidget(int x, int y, int width, int height, Component message, Font font) {
        super(x, y, width, height, message);
        this.font = font;
    }

    public abstract void visitLines(ActiveTextCollector var1);

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        GuiGraphics.HoveredTextEffects effects = this.isHovered() ? (this.componentClickHandler != null ? GuiGraphics.HoveredTextEffects.TOOLTIP_AND_CURSOR : GuiGraphics.HoveredTextEffects.TOOLTIP_ONLY) : GuiGraphics.HoveredTextEffects.NONE;
        this.visitLines(graphics.textRendererForWidget(this, effects));
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        if (this.componentClickHandler != null) {
            ActiveTextCollector.ClickableStyleFinder finder = new ActiveTextCollector.ClickableStyleFinder(this.getFont(), (int)event.x(), (int)event.y());
            this.visitLines(finder);
            Style clickedStyle = finder.result();
            if (clickedStyle != null) {
                this.componentClickHandler.accept(clickedStyle);
                return;
            }
        }
        super.onClick(event, doubleClick);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }

    protected final Font getFont() {
        return this.font;
    }

    @Override
    public void setMessage(Component message) {
        super.setMessage(message);
        this.setWidth(this.getFont().width(message.getVisualOrderText()));
    }

    public AbstractStringWidget setComponentClickHandler(@Nullable Consumer<Style> clickEventConsumer) {
        this.componentClickHandler = clickEventConsumer;
        return this;
    }
}

