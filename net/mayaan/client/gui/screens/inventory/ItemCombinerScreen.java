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
import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.inventory.ContainerListener;
import net.mayaan.world.inventory.ItemCombinerMenu;
import net.mayaan.world.item.ItemStack;

public abstract class ItemCombinerScreen<T extends ItemCombinerMenu>
extends AbstractContainerScreen<T>
implements ContainerListener {
    private final Identifier menuResource;

    public ItemCombinerScreen(T menu, Inventory inventory, Component title, Identifier menuResource) {
        super(menu, inventory, title);
        this.menuResource = menuResource;
    }

    protected void subInit() {
    }

    @Override
    protected void init() {
        super.init();
        this.subInit();
        ((ItemCombinerMenu)this.menu).addSlotListener(this);
    }

    @Override
    public void removed() {
        super.removed();
        ((ItemCombinerMenu)this.menu).removeSlotListener(this);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float a, int xm, int ym) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, this.menuResource, this.leftPos, this.topPos, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
        this.renderErrorIcon(graphics, this.leftPos, this.topPos);
    }

    protected abstract void renderErrorIcon(GuiGraphics var1, int var2, int var3);

    @Override
    public void dataChanged(AbstractContainerMenu container, int id, int value) {
    }

    @Override
    public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {
    }
}

