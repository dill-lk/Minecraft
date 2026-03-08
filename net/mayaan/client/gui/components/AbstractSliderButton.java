/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.components;

import com.maayanlabs.blaze3d.platform.cursor.CursorTypes;
import net.mayaan.client.InputType;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.narration.NarratedElementType;
import net.mayaan.client.gui.narration.NarrationElementOutput;
import net.mayaan.client.input.KeyEvent;
import net.mayaan.client.input.MouseButtonEvent;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.client.sounds.SoundManager;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.resources.Identifier;
import net.mayaan.util.ARGB;
import net.mayaan.util.Mth;

public abstract class AbstractSliderButton
extends AbstractWidget.WithInactiveMessage {
    private static final Identifier SLIDER_SPRITE = Identifier.withDefaultNamespace("widget/slider");
    private static final Identifier HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/slider_highlighted");
    private static final Identifier SLIDER_HANDLE_SPRITE = Identifier.withDefaultNamespace("widget/slider_handle");
    private static final Identifier SLIDER_HANDLE_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/slider_handle_highlighted");
    protected static final int TEXT_MARGIN = 2;
    public static final int DEFAULT_HEIGHT = 20;
    protected static final int HANDLE_WIDTH = 8;
    private static final int HANDLE_HALF_WIDTH = 4;
    protected double value;
    protected boolean canChangeValue;
    private boolean dragging;

    public AbstractSliderButton(int x, int y, int width, int height, Component message, double initialValue) {
        super(x, y, width, height, message);
        this.value = initialValue;
    }

    private Identifier getSprite() {
        if (this.isActive() && this.isFocused() && !this.canChangeValue) {
            return HIGHLIGHTED_SPRITE;
        }
        return SLIDER_SPRITE;
    }

    private Identifier getHandleSprite() {
        if (this.isActive() && (this.isHovered || this.canChangeValue)) {
            return SLIDER_HANDLE_HIGHLIGHTED_SPRITE;
        }
        return SLIDER_HANDLE_SPRITE;
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        return Component.translatable("gui.narrate.slider", this.getMessage());
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, (Component)this.createNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                if (this.canChangeValue) {
                    output.add(NarratedElementType.USAGE, (Component)Component.translatable("narration.slider.usage.focused"));
                } else {
                    output.add(NarratedElementType.USAGE, (Component)Component.translatable("narration.slider.usage.focused.keyboard_cannot_change_value"));
                }
            } else {
                output.add(NarratedElementType.USAGE, (Component)Component.translatable("narration.slider.usage.hovered"));
            }
        }
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.getSprite(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), ARGB.white(this.alpha));
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.getHandleSprite(), this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 8, this.getHeight(), ARGB.white(this.alpha));
        this.renderScrollingStringOverContents(graphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE), this.getMessage(), 2);
        this.handleCursor(graphics);
    }

    @Override
    protected void handleCursor(GuiGraphics graphics) {
        if (this.isHovered()) {
            graphics.requestCursor(this.isActive() ? (this.dragging ? CursorTypes.RESIZE_EW : CursorTypes.POINTING_HAND) : CursorTypes.NOT_ALLOWED);
        }
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        this.dragging = this.active;
        this.setValueFromMouse(event);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            this.canChangeValue = false;
            return;
        }
        InputType lastInputType = Mayaan.getInstance().getLastInputType();
        if (lastInputType == InputType.MOUSE || lastInputType == InputType.KEYBOARD_TAB) {
            this.canChangeValue = true;
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isSelection()) {
            this.canChangeValue = !this.canChangeValue;
            return true;
        }
        if (this.canChangeValue) {
            boolean left = event.isLeft();
            boolean right = event.isRight();
            if (left || right) {
                float direction = left ? -1.0f : 1.0f;
                this.setValue(this.value + (double)(direction / (float)(this.width - 8)));
                return true;
            }
        }
        return false;
    }

    private void setValueFromMouse(MouseButtonEvent event) {
        this.setValue((event.x() - (double)(this.getX() + 4)) / (double)(this.width - 8));
    }

    protected void setValue(double newValue) {
        double oldValue = this.value;
        this.value = Mth.clamp(newValue, 0.0, 1.0);
        if (oldValue != this.value) {
            this.applyValue();
        }
        this.updateMessage();
    }

    @Override
    protected void onDrag(MouseButtonEvent event, double dx, double dy) {
        this.setValueFromMouse(event);
        super.onDrag(event, dx, dy);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    public void onRelease(MouseButtonEvent event) {
        this.dragging = false;
        super.playDownSound(Mayaan.getInstance().getSoundManager());
    }

    protected abstract void updateMessage();

    protected abstract void applyValue();
}

