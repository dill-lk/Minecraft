/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components;

import java.util.function.Supplier;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.ActiveTextCollector;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.components.WidgetSprites;
import net.mayaan.client.input.InputWithModifiers;
import net.mayaan.client.input.KeyEvent;
import net.mayaan.client.input.MouseButtonEvent;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.util.ARGB;
import org.jspecify.annotations.Nullable;

public abstract class AbstractButton
extends AbstractWidget.WithInactiveMessage {
    protected static final int TEXT_MARGIN = 2;
    private static final WidgetSprites SPRITES = new WidgetSprites(Identifier.withDefaultNamespace("widget/button"), Identifier.withDefaultNamespace("widget/button_disabled"), Identifier.withDefaultNamespace("widget/button_highlighted"));
    private @Nullable Supplier<Boolean> overrideRenderHighlightedSprite;

    public AbstractButton(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    public abstract void onPress(InputWithModifiers var1);

    @Override
    protected final void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        this.renderContents(graphics, mouseX, mouseY, a);
        this.handleCursor(graphics);
    }

    protected abstract void renderContents(GuiGraphics var1, int var2, int var3, float var4);

    protected void renderDefaultLabel(ActiveTextCollector output) {
        this.renderScrollingStringOverContents(output, this.getMessage(), 2);
    }

    protected final void renderDefaultSprite(GuiGraphics graphics) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITES.get(this.active, this.overrideRenderHighlightedSprite != null ? this.overrideRenderHighlightedSprite.get().booleanValue() : this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight(), ARGB.white(this.alpha));
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        this.onPress(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (!this.isActive()) {
            return false;
        }
        if (event.isSelection()) {
            this.playDownSound(Mayaan.getInstance().getSoundManager());
            this.onPress(event);
            return true;
        }
        return false;
    }

    public void setOverrideRenderHighlightedSprite(Supplier<Boolean> overrideRenderHighlightedSprite) {
        this.overrideRenderHighlightedSprite = overrideRenderHighlightedSprite;
    }
}

