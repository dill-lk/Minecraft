/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.components.toasts;

import java.util.ArrayList;
import java.util.List;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.toasts.Toast;
import net.mayaan.client.gui.components.toasts.ToastManager;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.util.context.ContextMap;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.crafting.display.RecipeDisplay;
import net.mayaan.world.item.crafting.display.SlotDisplayContext;

public class RecipeToast
implements Toast {
    private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/recipe");
    private static final long DISPLAY_TIME = 5000L;
    private static final Component TITLE_TEXT = Component.translatable("recipe.toast.title");
    private static final Component DESCRIPTION_TEXT = Component.translatable("recipe.toast.description");
    private final List<Entry> recipeItems = new ArrayList<Entry>();
    private long lastChanged;
    private boolean changed;
    private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;
    private int displayedRecipeIndex;

    private RecipeToast() {
    }

    @Override
    public Toast.Visibility getWantedVisibility() {
        return this.wantedVisibility;
    }

    @Override
    public void update(ToastManager manager, long fullyVisibleForMs) {
        if (this.changed) {
            this.lastChanged = fullyVisibleForMs;
            this.changed = false;
        }
        this.wantedVisibility = this.recipeItems.isEmpty() ? Toast.Visibility.HIDE : ((double)(fullyVisibleForMs - this.lastChanged) >= 5000.0 * manager.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW);
        this.displayedRecipeIndex = (int)((double)fullyVisibleForMs / Math.max(1.0, 5000.0 * manager.getNotificationDisplayTimeMultiplier() / (double)this.recipeItems.size()) % (double)this.recipeItems.size());
    }

    @Override
    public void render(GuiGraphics graphics, Font font, long fullyVisibleForMs) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
        graphics.drawString(font, TITLE_TEXT, 30, 7, -11534256, false);
        graphics.drawString(font, DESCRIPTION_TEXT, 30, 18, -16777216, false);
        Entry items = this.recipeItems.get(this.displayedRecipeIndex);
        graphics.pose().pushMatrix();
        graphics.pose().scale(0.6f, 0.6f);
        graphics.renderFakeItem(items.categoryItem(), 3, 3);
        graphics.pose().popMatrix();
        graphics.renderFakeItem(items.unlockedItem(), 8, 8);
    }

    private void addItem(ItemStack craftingStation, ItemStack unlockedItem) {
        this.recipeItems.add(new Entry(craftingStation, unlockedItem));
        this.changed = true;
    }

    public static void addOrUpdate(ToastManager toastManager, RecipeDisplay recipe) {
        RecipeToast toast = toastManager.getToast(RecipeToast.class, NO_TOKEN);
        if (toast == null) {
            toast = new RecipeToast();
            toastManager.addToast(toast);
        }
        ContextMap context = SlotDisplayContext.fromLevel(toastManager.getMinecraft().level);
        ItemStack categoryItem = recipe.craftingStation().resolveForFirstStack(context);
        ItemStack unlockedItem = recipe.result().resolveForFirstStack(context);
        toast.addItem(categoryItem, unlockedItem);
    }

    private record Entry(ItemStack categoryItem, ItemStack unlockedItem) {
    }
}

