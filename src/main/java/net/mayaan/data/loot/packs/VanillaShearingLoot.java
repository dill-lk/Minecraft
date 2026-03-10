/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.loot.packs;

import java.util.function.BiConsumer;
import net.mayaan.advancements.criterion.DataComponentMatchers;
import net.mayaan.advancements.criterion.EntityPredicate;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.component.DataComponentExactPredicate;
import net.mayaan.core.component.DataComponents;
import net.mayaan.data.loot.EntityLootSubProvider;
import net.mayaan.data.loot.LootTableSubProvider;
import net.mayaan.data.loot.packs.LootData;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.entity.animal.cow.MushroomCow;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.storage.loot.BuiltInLootTables;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.LootPool;
import net.mayaan.world.level.storage.loot.LootTable;
import net.mayaan.world.level.storage.loot.entries.AlternativesEntry;
import net.mayaan.world.level.storage.loot.entries.LootItem;
import net.mayaan.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.mayaan.world.level.storage.loot.entries.NestedLootTable;
import net.mayaan.world.level.storage.loot.functions.SetItemCountFunction;
import net.mayaan.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.mayaan.world.level.storage.loot.providers.number.ConstantValue;
import net.mayaan.world.level.storage.loot.providers.number.UniformGenerator;

public record VanillaShearingLoot(HolderLookup.Provider registries) implements LootTableSubProvider
{
    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(BuiltInLootTables.BOGGED_SHEAR, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2.0f)).add((LootPoolEntryContainer.Builder<?>)((Object)LootItem.lootTableItem(Items.BROWN_MUSHROOM).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f))))).add((LootPoolEntryContainer.Builder<?>)((Object)LootItem.lootTableItem(Items.RED_MUSHROOM).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))))));
        LootData.WOOL_ITEM_BY_DYE.forEach((dye, wool) -> output.accept(BuiltInLootTables.SHEAR_SHEEP_BY_DYE.get(dye), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0f, 3.0f)).add(LootItem.lootTableItem(wool)))));
        output.accept(BuiltInLootTables.SHEAR_SHEEP, LootTable.lootTable().withPool(EntityLootSubProvider.createSheepDispatchPool(BuiltInLootTables.SHEAR_SHEEP_BY_DYE)));
        output.accept(BuiltInLootTables.SHEAR_MOOSHROOM, LootTable.lootTable().withPool(LootPool.lootPool().add(AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[]{NestedLootTable.lootTableReference(BuiltInLootTables.SHEAR_RED_MOOSHROOM).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.MOOSHROOM_VARIANT, MushroomCow.Variant.RED)).build()))), NestedLootTable.lootTableReference(BuiltInLootTables.SHEAR_BROWN_MOOSHROOM).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.MOOSHROOM_VARIANT, MushroomCow.Variant.BROWN)).build())))}))));
        output.accept(BuiltInLootTables.SHEAR_RED_MOOSHROOM, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0f)).add(LootItem.lootTableItem(Items.RED_MUSHROOM))));
        output.accept(BuiltInLootTables.SHEAR_BROWN_MOOSHROOM, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0f)).add(LootItem.lootTableItem(Items.BROWN_MUSHROOM))));
        output.accept(BuiltInLootTables.SHEAR_SNOW_GOLEM, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(Items.CARVED_PUMPKIN))));
    }
}

