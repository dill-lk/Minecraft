/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.loot.packs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import net.mayaan.advancements.criterion.EntityPredicate;
import net.mayaan.advancements.criterion.EntityTypePredicate;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.loot.LootTableSubProvider;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.storage.loot.BuiltInLootTables;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.LootPool;
import net.mayaan.world.level.storage.loot.LootTable;
import net.mayaan.world.level.storage.loot.entries.AlternativesEntry;
import net.mayaan.world.level.storage.loot.entries.LootItem;
import net.mayaan.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.mayaan.world.level.storage.loot.entries.NestedLootTable;
import net.mayaan.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.mayaan.world.level.storage.loot.predicates.LootItemCondition;
import net.mayaan.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.mayaan.world.level.storage.loot.providers.number.ConstantValue;

public record VanillaChargedCreeperExplosionLoot(HolderLookup.Provider registries) implements LootTableSubProvider
{
    private static final List<Entry> ENTRIES = List.of(new Entry(BuiltInLootTables.CHARGED_CREEPER_PIGLIN, EntityType.PIGLIN, Items.PIGLIN_HEAD), new Entry(BuiltInLootTables.CHARGED_CREEPER_CREEPER, EntityType.CREEPER, Items.CREEPER_HEAD), new Entry(BuiltInLootTables.CHARGED_CREEPER_SKELETON, EntityType.SKELETON, Items.SKELETON_SKULL), new Entry(BuiltInLootTables.CHARGED_CREEPER_WITHER_SKELETON, EntityType.WITHER_SKELETON, Items.WITHER_SKELETON_SKULL), new Entry(BuiltInLootTables.CHARGED_CREEPER_ZOMBIE, EntityType.ZOMBIE, Items.ZOMBIE_HEAD));

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        HolderGetter entityTypes = this.registries.lookupOrThrow(Registries.ENTITY_TYPE);
        ArrayList<ConditionUserBuilder> alternatives = new ArrayList<ConditionUserBuilder>(ENTRIES.size());
        for (Entry entry : ENTRIES) {
            output.accept(entry.lootTable, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(entry.item))));
            LootItemCondition.Builder predicate = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, entry.entityType)));
            alternatives.add(NestedLootTable.lootTableReference(entry.lootTable).when(predicate));
        }
        output.accept(BuiltInLootTables.CHARGED_CREEPER, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(AlternativesEntry.alternatives((LootPoolEntryContainer.Builder[])alternatives.toArray(LootPoolEntryContainer.Builder[]::new)))));
    }

    private record Entry(ResourceKey<LootTable> lootTable, EntityType<?> entityType, Item item) {
    }
}

