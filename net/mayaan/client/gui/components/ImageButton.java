/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.components;

import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.WidgetSprites;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;

public class ImageButton
extends Button {
    protected final WidgetSprites sprites;

    public ImageButton(int x, int y, int width, int height, WidgetSprites sprites, Button.OnPress onPress) {
        this(x, y, width, height, sprites, onPress, CommonComponents.EMPTY);
    }

    public ImageButton(int width, int height, WidgetSprites sprites, Button.OnPress onPress, Component message) {
        this(0, 0, width, height, sprites, onPress, message);
    }

    public ImageButton(int x, int y, int width, int height, WidgetSprites sprites, Button.OnPress onPress, Component message) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.sprites = sprites;
    }

    @Override
    public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        Identifier sprite = this.sprites.get(this.isActive(), this.isHoveredOrFocused());
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, this.getX(), this.getY(), this.width, this.height);
    }
}

