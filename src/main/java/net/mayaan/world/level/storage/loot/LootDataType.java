/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.storage.loot;

import com.mojang.serialization.Codec;
import java.util.stream.Stream;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.Registry;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.ResourceKey;
import net.mayaan.util.ProblemReporter;
import net.mayaan.util.context.ContextKeySet;
import net.mayaan.world.level.storage.loot.LootTable;
import net.mayaan.world.level.storage.loot.Validatable;
import net.mayaan.world.level.storage.loot.ValidationContext;
import net.mayaan.world.level.storage.loot.ValidationContextSource;
import net.mayaan.world.level.storage.loot.functions.LootItemFunction;
import net.mayaan.world.level.storage.loot.functions.LootItemFunctions;
import net.mayaan.world.level.storage.loot.parameters.LootContextParamSets;
import net.mayaan.world.level.storage.loot.predicates.LootItemCondition;

public record LootDataType<T extends Validatable>(ResourceKey<Registry<T>> registryKey, Codec<T> codec, ContextGetter<T> contextGetter) {
    public static final LootDataType<LootItemCondition> PREDICATE = new LootDataType<LootItemCondition>(Registries.PREDICATE, LootItemCondition.DIRECT_CODEC, ContextGetter.constant(LootContextParamSets.ALL_PARAMS));
    public static final LootDataType<LootItemFunction> MODIFIER = new LootDataType<LootItemFunction>(Registries.ITEM_MODIFIER, LootItemFunctions.ROOT_CODEC, ContextGetter.constant(LootContextParamSets.ALL_PARAMS));
    public static final LootDataType<LootTable> TABLE = new LootDataType<LootTable>(Registries.LOOT_TABLE, LootTable.DIRECT_CODEC, LootTable::getParamSet);

    public void runValidation(ValidationContextSource contextSource, ResourceKey<T> key, T value) {
        ContextKeySet contextKeys = this.contextGetter.context(value);
        ValidationContext rootContext = contextSource.context(contextKeys).enterElement(new ProblemReporter.RootElementPathElement(key), key);
        value.validate(rootContext);
    }

    public void runValidation(ValidationContextSource contextSource, HolderLookup<T> lookup) {
        lookup.listElements().forEach(holder -> this.runValidation(contextSource, holder.key(), (Validatable)holder.value()));
    }

    public static Stream<LootDataType<?>> values() {
        return Stream.of(PREDICATE, MODIFIER, TABLE);
    }

    @FunctionalInterface
    public static interface ContextGetter<T> {
        public ContextKeySet context(T var1);

        public static <T> ContextGetter<T> constant(ContextKeySet v) {
            return value -> v;
        }
    }
}

