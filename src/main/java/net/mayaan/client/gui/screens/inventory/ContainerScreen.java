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
import net.mayaan.world.inventory.ChestMenu;

public class ContainerScreen
extends AbstractContainerScreen<ChestMenu> {
    private static final Identifier CONTAINER_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");
    private final int containerRows;

    public ContainerScreen(ChestMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 114 + menu.getRowCount() * 18);
        this.containerRows = menu.getRowCount();
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, xo, yo, 0.0f, 0.0f, this.imageWidth, this.containerRows * 18 + 17, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, xo, yo + this.containerRows * 18 + 17, 0.0f, 126.0f, this.imageWidth, 96, 256, 256);
    }
}

