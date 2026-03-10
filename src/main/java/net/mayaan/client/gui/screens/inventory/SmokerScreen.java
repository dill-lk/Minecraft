/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.inventory;

import java.util.List;
import net.mayaan.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.mayaan.client.gui.screens.recipebook.RecipeBookComponent;
import net.mayaan.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.inventory.SmokerMenu;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.crafting.RecipeBookCategories;

public class SmokerScreen
extends AbstractFurnaceScreen<SmokerMenu> {
    private static final Identifier LIT_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/smoker/lit_progress");
    private static final Identifier BURN_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/smoker/burn_progress");
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/smoker.png");
    private static final Component FILTER_NAME = Component.translatable("gui.recipebook.toggleRecipes.smokable");
    private static final List<RecipeBookComponent.TabInfo> TABS = List.of(new RecipeBookComponent.TabInfo(SearchRecipeBookCategory.SMOKER), new RecipeBookComponent.TabInfo(Items.PORKCHOP, RecipeBookCategories.SMOKER_FOOD));

    public SmokerScreen(SmokerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, FILTER_NAME, TEXTURE, LIT_PROGRESS_SPRITE, BURN_PROGRESS_SPRITE, TABS);
    }
}

