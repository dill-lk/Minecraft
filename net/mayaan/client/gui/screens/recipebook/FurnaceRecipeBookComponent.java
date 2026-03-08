/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.recipebook;

import java.util.List;
import net.mayaan.client.gui.components.WidgetSprites;
import net.mayaan.client.gui.screens.recipebook.GhostSlots;
import net.mayaan.client.gui.screens.recipebook.RecipeBookComponent;
import net.mayaan.client.gui.screens.recipebook.RecipeCollection;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.util.context.ContextMap;
import net.mayaan.world.entity.player.StackedItemContents;
import net.mayaan.world.inventory.AbstractFurnaceMenu;
import net.mayaan.world.inventory.Slot;
import net.mayaan.world.item.crafting.display.FurnaceRecipeDisplay;
import net.mayaan.world.item.crafting.display.RecipeDisplay;

public class FurnaceRecipeBookComponent
extends RecipeBookComponent<AbstractFurnaceMenu> {
    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled"), Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled"), Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled_highlighted"), Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled_highlighted"));
    private final Component recipeFilterName;

    public FurnaceRecipeBookComponent(AbstractFurnaceMenu menu, Component recipeFilterName, List<RecipeBookComponent.TabInfo> tabInfos) {
        super(menu, tabInfos);
        this.recipeFilterName = recipeFilterName;
    }

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        return FILTER_SPRITES;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return switch (slot.index) {
            case 0, 1, 2 -> true;
            default -> false;
        };
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipe, ContextMap context) {
        ghostSlots.setResult(((AbstractFurnaceMenu)this.menu).getResultSlot(), context, recipe.result());
        if (recipe instanceof FurnaceRecipeDisplay) {
            FurnaceRecipeDisplay furnaceRecipe = (FurnaceRecipeDisplay)recipe;
            ghostSlots.setInput((Slot)((AbstractFurnaceMenu)this.menu).slots.get(0), context, furnaceRecipe.ingredient());
            Slot fuelSlot = (Slot)((AbstractFurnaceMenu)this.menu).slots.get(1);
            if (fuelSlot.getItem().isEmpty()) {
                ghostSlots.setInput(fuelSlot, context, furnaceRecipe.fuel());
            }
        }
    }

    @Override
    protected Component getRecipeFilterName() {
        return this.recipeFilterName;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents stackedContents) {
        collection.selectRecipes(stackedContents, display -> display instanceof FurnaceRecipeDisplay);
    }
}

