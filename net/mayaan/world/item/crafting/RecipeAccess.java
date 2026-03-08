/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.crafting;

import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.crafting.RecipePropertySet;
import net.mayaan.world.item.crafting.SelectableRecipe;
import net.mayaan.world.item.crafting.StonecutterRecipe;

public interface RecipeAccess {
    public RecipePropertySet propertySet(ResourceKey<RecipePropertySet> var1);

    public SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes();
}

