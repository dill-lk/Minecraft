/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.components;

import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.narration.NarratedElementType;
import net.mayaan.client.gui.narration.NarrationElementOutput;
import net.mayaan.network.chat.Component;
import net.mayaan.world.item.ItemStack;

public class ItemDisplayWidget
extends AbstractWidget {
    private final Mayaan minecraft;
    private final int offsetX;
    private final int offsetY;
    private final ItemStack itemStack;
    private final boolean decorations;
    private final boolean tooltip;

    public ItemDisplayWidget(Mayaan minecraft, int offsetX, int offsetY, int width, int height, Component message, ItemStack itemStack, boolean decorations, boolean tooltip) {
        super(0, 0, width, height, message);
        this.minecraft = minecraft;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.itemStack = itemStack;
        this.decorations = decorations;
        this.tooltip = tooltip;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        graphics.renderItem(this.itemStack, this.getX() + this.offsetX, this.getY() + this.offsetY, 0);
        if (this.decorations) {
            graphics.renderItemDecorations(this.minecraft.font, this.itemStack, this.getX() + this.offsetX, this.getY() + this.offsetY, null);
        }
        if (this.isFocused()) {
            graphics.renderOutline(this.getX(), this.getY(), this.getWidth(), this.getHeight(), -1);
        }
        if (this.tooltip && this.isHovered()) {
            this.renderTooltip(graphics, mouseX, mouseY);
        }
    }

    protected void renderTooltip(GuiGraphics graphics, int x, int y) {
        graphics.setTooltipForNextFrame(this.minecraft.font, this.itemStack, x, y);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, (Component)Component.translatable("narration.item", this.itemStack.getHoverName()));
    }
}

