/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector3f
 */
package net.mayaan.client.gui.screens.inventory;

import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.screens.inventory.AbstractSignEditScreen;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.world.level.block.entity.SignBlockEntity;
import org.joml.Vector3f;

public class HangingSignEditScreen
extends AbstractSignEditScreen {
    public static final float MAGIC_BACKGROUND_SCALE = 4.5f;
    private static final Vector3f TEXT_SCALE = new Vector3f(1.0f, 1.0f, 1.0f);
    private static final int TEXTURE_WIDTH = 16;
    private static final int TEXTURE_HEIGHT = 16;
    private final Identifier texture;

    public HangingSignEditScreen(SignBlockEntity sign, boolean isFrontText, boolean shouldFilter) {
        super(sign, isFrontText, shouldFilter, Component.translatable("hanging_sign.edit"));
        this.texture = Identifier.withDefaultNamespace("textures/gui/hanging_signs/" + this.woodType.name() + ".png");
    }

    @Override
    protected float getSignYOffset() {
        return 125.0f;
    }

    @Override
    protected void renderSignBackground(GuiGraphics graphics) {
        graphics.pose().translate(0.0f, -13.0f);
        graphics.pose().scale(4.5f, 4.5f);
        graphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, -8, -8, 0.0f, 0.0f, 16, 16, 16, 16);
    }

    @Override
    protected Vector3f getSignTextScale() {
        return TEXT_SCALE;
    }
}

