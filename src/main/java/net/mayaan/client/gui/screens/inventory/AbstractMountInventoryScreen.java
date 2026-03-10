/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.inventory;

import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.screens.inventory.AbstractContainerScreen;
import net.mayaan.client.gui.screens.inventory.InventoryScreen;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.inventory.AbstractMountInventoryMenu;
import org.jspecify.annotations.Nullable;

public abstract class AbstractMountInventoryScreen<T extends AbstractMountInventoryMenu>
extends AbstractContainerScreen<T> {
    protected final int inventoryColumns;
    protected float xMouse;
    protected float yMouse;
    protected final LivingEntity mount;

    public AbstractMountInventoryScreen(T menu, Inventory inventory, Component title, int inventoryColumns, LivingEntity mount) {
        super(menu, inventory, title);
        this.inventoryColumns = inventoryColumns;
        this.mount = mount;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, this.getBackgroundTextureLocation(), xo, yo, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
        if (this.inventoryColumns > 0 && this.getChestSlotsSpriteLocation() != null) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.getChestSlotsSpriteLocation(), 90, 54, 0, 0, xo + 79, yo + 17, this.inventoryColumns * 18, 54);
        }
        if (this.shouldRenderSaddleSlot()) {
            this.drawSlot(graphics, xo + 7, yo + 35 - 18);
        }
        if (this.shouldRenderArmorSlot()) {
            this.drawSlot(graphics, xo + 7, yo + 35);
        }
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, xo + 26, yo + 18, xo + 78, yo + 70, 17, 0.25f, this.xMouse, this.yMouse, this.mount);
    }

    protected void drawSlot(GuiGraphics graphics, int x, int y) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.getSlotSpriteLocation(), x, y, 18, 18);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        this.xMouse = mouseX;
        this.yMouse = mouseY;
        super.render(graphics, mouseX, mouseY, a);
    }

    protected abstract Identifier getBackgroundTextureLocation();

    protected abstract Identifier getSlotSpriteLocation();

    protected abstract @Nullable Identifier getChestSlotsSpriteLocation();

    protected abstract boolean shouldRenderSaddleSlot();

    protected abstract boolean shouldRenderArmorSlot();
}

