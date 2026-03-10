/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.loot.packs;

import java.util.function.BiConsumer;
import net.mayaan.core.HolderLookup;
import net.mayaan.data.loot.LootTableSubProvider;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.storage.loot.BuiltInLootTables;
import net.mayaan.world.level.storage.loot.LootPool;
import net.mayaan.world.level.storage.loot.LootTable;
import net.mayaan.world.level.storage.loot.entries.LootItem;
import net.mayaan.world.level.storage.loot.providers.number.ConstantValue;

public record VanillaEntityInteractLoot(HolderLookup.Provider registries) implements LootTableSubProvider
{
    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(BuiltInLootTables.ARMADILLO_BRUSH, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(Items.ARMADILLO_SCUTE))));
    }
}

