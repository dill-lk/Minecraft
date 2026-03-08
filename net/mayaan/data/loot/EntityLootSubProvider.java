/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.mayaan.data.loot;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import net.mayaan.advancements.criterion.DamageSourcePredicate;
import net.mayaan.advancements.criterion.DataComponentMatchers;
import net.mayaan.advancements.criterion.EnchantmentPredicate;
import net.mayaan.advancements.criterion.EntityEquipmentPredicate;
import net.mayaan.advancements.criterion.EntityFlagsPredicate;
import net.mayaan.advancements.criterion.EntityPredicate;
import net.mayaan.advancements.criterion.ItemPredicate;
import net.mayaan.advancements.criterion.MinMaxBounds;
import net.mayaan.advancements.criterion.SheepPredicate;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.component.DataComponentExactPredicate;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.component.predicates.DataComponentPredicates;
import net.mayaan.core.component.predicates.EnchantmentsPredicate;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.loot.LootTableSubProvider;
import net.mayaan.resources.ResourceKey;
import net.mayaan.tags.EnchantmentTags;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.animal.frog.FrogVariant;
import net.mayaan.world.flag.FeatureFlagSet;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.LootPool;
import net.mayaan.world.level.storage.loot.LootTable;
import net.mayaan.world.level.storage.loot.entries.AlternativesEntry;
import net.mayaan.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.mayaan.world.level.storage.loot.entries.NestedLootTable;
import net.mayaan.world.level.storage.loot.predicates.AnyOfCondition;
import net.mayaan.world.level.storage.loot.predicates.DamageSourceCondition;
import net.mayaan.world.level.storage.loot.predicates.LootItemCondition;
import net.mayaan.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

public abstract class EntityLootSubProvider
implements LootTableSubProvider {
    protected final HolderLookup.Provider registries;
    private final FeatureFlagSet allowed;
    private final FeatureFlagSet required;
    private final Map<EntityType<?>, Map<ResourceKey<LootTable>, LootTable.Builder>> map = Maps.newHashMap();

    protected final AnyOfCondition.Builder shouldSmeltLoot() {
        HolderGetter enchantmentsRegistry = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return AnyOfCondition.anyOf(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true))), LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().withComponents(DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(enchantmentsRegistry.getOrThrow(EnchantmentTags.SMELTS_LOOT), MinMaxBounds.Ints.ANY)))).build())))));
    }

    protected EntityLootSubProvider(FeatureFlagSet enabledFeatures, HolderLookup.Provider registries) {
        this(enabledFeatures, enabledFeatures, registries);
    }

    protected EntityLootSubProvider(FeatureFlagSet allowed, FeatureFlagSet required, HolderLookup.Provider registries) {
        this.allowed = allowed;
        this.required = required;
        this.registries = registries;
    }

    public static LootPool.Builder createSheepDispatchPool(Map<DyeColor, ResourceKey<LootTable>> tableNames) {
        AlternativesEntry.Builder variants = AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[0]);
        for (Map.Entry<DyeColor, ResourceKey<LootTable>> e : tableNames.entrySet()) {
            variants = variants.otherwise((LootPoolEntryContainer.Builder<?>)NestedLootTable.lootTableReference(e.getValue()).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.SHEEP_COLOR, e.getKey())).build()).subPredicate(SheepPredicate.hasWool()))));
        }
        return LootPool.lootPool().add(variants);
    }

    public abstract void generate();

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        this.generate();
        HashSet seen = new HashSet();
        BuiltInRegistries.ENTITY_TYPE.listElements().forEach(holder -> {
            EntityType type = (EntityType)holder.value();
            if (!type.isEnabled(this.allowed)) {
                return;
            }
            Optional<ResourceKey<LootTable>> defaultLootTable = type.getDefaultLootTable();
            if (defaultLootTable.isPresent()) {
                Map<ResourceKey<LootTable>, LootTable.Builder> builders = this.map.remove(type);
                if (type.isEnabled(this.required) && (builders == null || !builders.containsKey(defaultLootTable.get()))) {
                    throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", defaultLootTable.get(), holder.key().identifier()));
                }
                if (builders != null) {
                    builders.forEach((id, builder) -> {
                        if (!seen.add(id)) {
                            throw new IllegalStateException(String.format(Locale.ROOT, "Duplicate loottable '%s' for '%s'", id, holder.key().identifier()));
                        }
                        output.accept((ResourceKey<LootTable>)id, (LootTable.Builder)builder);
                    });
                }
            } else {
                Map<ResourceKey<LootTable>, LootTable.Builder> builders = this.map.remove(type);
                if (builders != null) {
                    throw new IllegalStateException(String.format(Locale.ROOT, "Weird loottables '%s' for '%s', not a LivingEntity so should not have loot", builders.keySet().stream().map(r -> r.identifier().toString()).collect(Collectors.joining(",")), holder.key().identifier()));
                }
            }
        });
        if (!this.map.isEmpty()) {
            throw new IllegalStateException("Created loot tables for entities not supported by datapack: " + String.valueOf(this.map.keySet()));
        }
    }

    protected LootItemCondition.Builder killedByFrog(HolderGetter<EntityType<?>> entityTypes) {
        return DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().source(EntityPredicate.Builder.entity().of(entityTypes, EntityType.FROG)));
    }

    protected LootItemCondition.Builder killedByFrogVariant(HolderGetter<EntityType<?>> entityTypes, HolderGetter<FrogVariant> frogVariants, ResourceKey<FrogVariant> variant) {
        return DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().source(EntityPredicate.Builder.entity().of(entityTypes, EntityType.FROG).components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.FROG_VARIANT, frogVariants.getOrThrow(variant))).build())));
    }

    protected void add(EntityType<?> type, LootTable.Builder builder) {
        this.add(type, type.getDefaultLootTable().orElseThrow(() -> new IllegalStateException("Entity " + String.valueOf(type) + " has no loot table")), builder);
    }

    protected void add(EntityType<?> type, ResourceKey<LootTable> lootTable, LootTable.Builder builder) {
        this.map.computeIfAbsent(type, k -> new HashMap()).put(lootTable, builder);
    }
}

