/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.loot;

import java.util.function.BiConsumer;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.storage.loot.LootTable;

@FunctionalInterface
public interface LootTableSubProvider {
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> var1);
}

