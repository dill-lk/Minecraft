/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.inventory;

import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.screens.inventory.AbstractContainerScreen;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.inventory.DispenserMenu;

public class DispenserScreen
extends AbstractContainerScreen<DispenserMenu> {
    private static final Identifier CONTAINER_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/dispenser.png");

    public DispenserScreen(DispenserMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_LOCATION, xo, yo, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
    }
}

