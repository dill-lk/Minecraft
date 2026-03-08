/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.crafting;

import net.mayaan.world.item.ItemStack;

public interface RecipeInput {
    public ItemStack getItem(int var1);

    public int size();

    default public boolean isEmpty() {
        for (int i = 0; i < this.size(); ++i) {
            if (this.getItem(i).isEmpty()) continue;
            return false;
        }
        return true;
    }
}

