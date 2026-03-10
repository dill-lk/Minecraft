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
import net.mayaan.world.inventory.GrindstoneMenu;

public class GrindstoneScreen
extends AbstractContainerScreen<GrindstoneMenu> {
    private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/grindstone/error");
    private static final Identifier GRINDSTONE_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/grindstone.png");

    public GrindstoneScreen(GrindstoneMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GRINDSTONE_LOCATION, xo, yo, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
        if ((((GrindstoneMenu)this.menu).getSlot(0).hasItem() || ((GrindstoneMenu)this.menu).getSlot(1).hasItem()) && !((GrindstoneMenu)this.menu).getSlot(2).hasItem()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, xo + 92, yo + 31, 28, 21);
        }
    }
}

