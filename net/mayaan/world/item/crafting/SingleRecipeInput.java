/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.crafting;

import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.crafting.RecipeInput;

public record SingleRecipeInput(ItemStack item) implements RecipeInput
{
    @Override
    public ItemStack getItem(int index) {
        if (index != 0) {
            throw new IllegalArgumentException("No item for index " + index);
        }
        return this.item;
    }

    @Override
    public int size() {
        return 1;
    }
}

