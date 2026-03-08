/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.data.recipes;

import net.mayaan.advancements.Advancement;
import net.mayaan.advancements.AdvancementHolder;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

public interface RecipeOutput {
    public void accept(ResourceKey<Recipe<?>> var1, Recipe<?> var2, @Nullable AdvancementHolder var3);

    public Advancement.Builder advancement();

    public void includeRootAdvancement();
}

