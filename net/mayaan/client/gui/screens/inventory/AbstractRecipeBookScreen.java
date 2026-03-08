/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.inventory;

import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.ImageButton;
import net.mayaan.client.gui.navigation.ScreenPosition;
import net.mayaan.client.gui.screens.inventory.AbstractContainerScreen;
import net.mayaan.client.gui.screens.recipebook.RecipeBookComponent;
import net.mayaan.client.gui.screens.recipebook.RecipeUpdateListener;
import net.mayaan.client.input.CharacterEvent;
import net.mayaan.client.input.KeyEvent;
import net.mayaan.client.input.MouseButtonEvent;
import net.mayaan.network.chat.Component;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.inventory.ContainerInput;
import net.mayaan.world.inventory.RecipeBookMenu;
import net.mayaan.world.inventory.Slot;
import net.mayaan.world.item.crafting.display.RecipeDisplay;

public abstract class AbstractRecipeBookScreen<T extends RecipeBookMenu>
extends AbstractContainerScreen<T>
implements RecipeUpdateListener {
    private final RecipeBookComponent<?> recipeBookComponent;
    private boolean widthTooNarrow;

    public AbstractRecipeBookScreen(T menu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.recipeBookComponent = recipeBookComponent;
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.initButton();
    }

    protected abstract ScreenPosition getRecipeBookButtonPosition();

    private void initButton() {
        ScreenPosition buttonPos = this.getRecipeBookButtonPosition();
        this.addRenderableWidget(new ImageButton(buttonPos.x(), buttonPos.y(), 20, 18, RecipeBookComponent.RECIPE_BUTTON_SPRITES, button -> {
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
            ScreenPosition updatedButtonPos = this.getRecipeBookButtonPosition();
            button.setPosition(updatedButtonPos.x(), updatedButtonPos.y());
            this.onRecipeBookButtonClick();
        }));
        this.addWidget(this.recipeBookComponent);
    }

    protected void onRecipeBookButtonClick() {
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBackground(graphics, mouseX, mouseY, a);
        } else {
            super.renderContents(graphics, mouseX, mouseY, a);
        }
        graphics.nextStratum();
        this.recipeBookComponent.render(graphics, mouseX, mouseY, a);
        graphics.nextStratum();
        this.renderCarriedItem(graphics, mouseX, mouseY);
        this.renderSnapbackItem(graphics);
        this.renderTooltip(graphics, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(graphics, mouseX, mouseY, this.hoveredSlot);
    }

    @Override
    protected void renderSlots(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderSlots(graphics, mouseX, mouseY);
        this.recipeBookComponent.renderGhostRecipe(graphics, this.isBiggerResultSlot());
    }

    protected boolean isBiggerResultSlot() {
        return true;
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (this.recipeBookComponent.charTyped(event)) {
            return true;
        }
        return super.charTyped(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.recipeBookComponent.keyPressed(event)) {
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (this.recipeBookComponent.mouseClicked(event, doubleClick)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        }
        if (this.widthTooNarrow && this.recipeBookComponent.isVisible()) {
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (this.recipeBookComponent.mouseDragged(event, dx, dy)) {
            return true;
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    protected boolean isHovering(int left, int top, int w, int h, double xm, double ym) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(left, top, w, h, xm, ym);
    }

    @Override
    protected boolean hasClickedOutside(double mx, double my, int xo, int yo) {
        boolean clickedOutside = mx < (double)xo || my < (double)yo || mx >= (double)(xo + this.imageWidth) || my >= (double)(yo + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(mx, my, this.leftPos, this.topPos, this.imageWidth, this.imageHeight) && clickedOutside;
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int buttonNum, ContainerInput containerInput) {
        super.slotClicked(slot, slotId, buttonNum, containerInput);
        this.recipeBookComponent.slotClicked(slot);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public void fillGhostRecipe(RecipeDisplay display) {
        this.recipeBookComponent.fillGhostRecipe(display);
    }
}

