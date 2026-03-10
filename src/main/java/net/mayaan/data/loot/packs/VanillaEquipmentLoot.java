/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.loot.packs;

import java.util.function.BiConsumer;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.loot.LootTableSubProvider;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.enchantment.Enchantment;
import net.mayaan.world.item.enchantment.Enchantments;
import net.mayaan.world.item.equipment.trim.ArmorTrim;
import net.mayaan.world.item.equipment.trim.TrimMaterials;
import net.mayaan.world.item.equipment.trim.TrimPatterns;
import net.mayaan.world.level.storage.loot.BuiltInLootTables;
import net.mayaan.world.level.storage.loot.LootPool;
import net.mayaan.world.level.storage.loot.LootTable;
import net.mayaan.world.level.storage.loot.entries.LootItem;
import net.mayaan.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.mayaan.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.mayaan.world.level.storage.loot.entries.NestedLootTable;
import net.mayaan.world.level.storage.loot.functions.SetComponentsFunction;
import net.mayaan.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.mayaan.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.mayaan.world.level.storage.loot.providers.number.ConstantValue;

public record VanillaEquipmentLoot(HolderLookup.Provider registries) implements LootTableSubProvider
{
    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        HolderGetter trimPatterns = this.registries.lookupOrThrow(Registries.TRIM_PATTERN);
        HolderGetter trimMaterials = this.registries.lookupOrThrow(Registries.TRIM_MATERIAL);
        HolderGetter enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        ArmorTrim flowTrim = new ArmorTrim(trimMaterials.getOrThrow(TrimMaterials.COPPER), trimPatterns.getOrThrow(TrimPatterns.FLOW));
        ArmorTrim boltTrim = new ArmorTrim(trimMaterials.getOrThrow(TrimMaterials.COPPER), trimPatterns.getOrThrow(TrimPatterns.BOLT));
        output.accept(BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add((LootPoolEntryContainer.Builder<?>)NestedLootTable.inlineLootTable(VanillaEquipmentLoot.trialChamberEquipment(Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, boltTrim, (HolderLookup.RegistryLookup<Enchantment>)enchantments).build()).setWeight(4)).add((LootPoolEntryContainer.Builder<?>)NestedLootTable.inlineLootTable(VanillaEquipmentLoot.trialChamberEquipment(Items.IRON_HELMET, Items.IRON_CHESTPLATE, flowTrim, (HolderLookup.RegistryLookup<Enchantment>)enchantments).build()).setWeight(2)).add((LootPoolEntryContainer.Builder<?>)NestedLootTable.inlineLootTable(VanillaEquipmentLoot.trialChamberEquipment(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, flowTrim, (HolderLookup.RegistryLookup<Enchantment>)enchantments).build()).setWeight(1))));
        output.accept(BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_MELEE, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(NestedLootTable.lootTableReference(BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add((LootPoolEntryContainer.Builder<?>)LootItem.lootTableItem(Items.IRON_SWORD).setWeight(4)).add((LootPoolEntryContainer.Builder<?>)((Object)LootItem.lootTableItem(Items.IRON_SWORD).apply(new SetEnchantmentsFunction.Builder().withEnchantment(enchantments.getOrThrow(Enchantments.SHARPNESS), ConstantValue.exactly(1.0f))))).add((LootPoolEntryContainer.Builder<?>)((Object)LootItem.lootTableItem(Items.IRON_SWORD).apply(new SetEnchantmentsFunction.Builder().withEnchantment(enchantments.getOrThrow(Enchantments.KNOCKBACK), ConstantValue.exactly(1.0f))))).add(LootItem.lootTableItem(Items.DIAMOND_SWORD))));
        output.accept(BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(NestedLootTable.lootTableReference(BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add((LootPoolEntryContainer.Builder<?>)LootItem.lootTableItem(Items.BOW).setWeight(2)).add((LootPoolEntryContainer.Builder<?>)((Object)LootItem.lootTableItem(Items.BOW).apply(new SetEnchantmentsFunction.Builder().withEnchantment(enchantments.getOrThrow(Enchantments.POWER), ConstantValue.exactly(1.0f))))).add((LootPoolEntryContainer.Builder<?>)((Object)LootItem.lootTableItem(Items.BOW).apply(new SetEnchantmentsFunction.Builder().withEnchantment(enchantments.getOrThrow(Enchantments.PUNCH), ConstantValue.exactly(1.0f)))))));
    }

    public static LootTable.Builder trialChamberEquipment(Item helmet, Item chestplate, ArmorTrim trim, HolderLookup.RegistryLookup<Enchantment> enchantments) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).when(LootItemRandomChanceCondition.randomChance(0.5f)).add((LootPoolEntryContainer.Builder<?>)((Object)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem(helmet).apply(SetComponentsFunction.setComponent(DataComponents.TRIM, trim))).apply(new SetEnchantmentsFunction.Builder().withEnchantment(enchantments.getOrThrow(Enchantments.PROTECTION), ConstantValue.exactly(4.0f)).withEnchantment(enchantments.getOrThrow(Enchantments.PROJECTILE_PROTECTION), ConstantValue.exactly(4.0f)).withEnchantment(enchantments.getOrThrow(Enchantments.FIRE_PROTECTION), ConstantValue.exactly(4.0f)))))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).when(LootItemRandomChanceCondition.randomChance(0.5f)).add((LootPoolEntryContainer.Builder<?>)((Object)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem(chestplate).apply(SetComponentsFunction.setComponent(DataComponents.TRIM, trim))).apply(new SetEnchantmentsFunction.Builder().withEnchantment(enchantments.getOrThrow(Enchantments.PROTECTION), ConstantValue.exactly(4.0f)).withEnchantment(enchantments.getOrThrow(Enchantments.PROJECTILE_PROTECTION), ConstantValue.exactly(4.0f)).withEnchantment(enchantments.getOrThrow(Enchantments.FIRE_PROTECTION), ConstantValue.exactly(4.0f))))));
    }
}

