/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.storage.loot.entries;

import java.util.function.Consumer;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.storage.loot.LootContext;

public interface LootPoolEntry {
    public int getWeight(float var1);

    public void createItemStack(Consumer<ItemStack> var1, LootContext var2);
}

