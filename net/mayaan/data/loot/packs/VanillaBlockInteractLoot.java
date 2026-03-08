/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.loot.packs;

import java.util.function.BiConsumer;
import net.mayaan.advancements.criterion.StatePropertiesPredicate;
import net.mayaan.core.HolderLookup;
import net.mayaan.data.loot.LootTableSubProvider;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.SweetBerryBushBlock;
import net.mayaan.world.level.storage.loot.BuiltInLootTables;
import net.mayaan.world.level.storage.loot.LootPool;
import net.mayaan.world.level.storage.loot.LootTable;
import net.mayaan.world.level.storage.loot.entries.LootItem;
import net.mayaan.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.mayaan.world.level.storage.loot.functions.SetItemCountFunction;
import net.mayaan.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.mayaan.world.level.storage.loot.providers.number.ConstantValue;
import net.mayaan.world.level.storage.loot.providers.number.UniformGenerator;

public record VanillaBlockInteractLoot(HolderLookup.Provider registries) implements LootTableSubProvider
{
    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(BuiltInLootTables.HARVEST_BEEHIVE, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add((LootPoolEntryContainer.Builder<?>)((Object)LootItem.lootTableItem(Items.HONEYCOMB).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0f)))))));
        output.accept(BuiltInLootTables.HARVEST_CAVE_VINE, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(Items.GLOW_BERRIES))));
        output.accept(BuiltInLootTables.HARVEST_SWEET_BERRY_BUSH, LootTable.lootTable().withPool(LootPool.lootPool().add((LootPoolEntryContainer.Builder<?>)((LootPoolEntryContainer.Builder)((Object)LootItem.lootTableItem(Items.SWEET_BERRIES).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f))))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SWEET_BERRY_BUSH).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 3))))).withPool(LootPool.lootPool().add((LootPoolEntryContainer.Builder<?>)((Object)LootItem.lootTableItem(Items.SWEET_BERRIES).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)))))));
        output.accept(BuiltInLootTables.CARVE_PUMPKIN, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add((LootPoolEntryContainer.Builder<?>)((Object)LootItem.lootTableItem(Items.PUMPKIN_SEEDS).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0f)))))));
    }
}

