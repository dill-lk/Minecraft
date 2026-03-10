/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.storage.loot.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.function.Predicate;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.RegistryFileCodec;
import net.mayaan.world.level.storage.loot.LootContext;
import net.mayaan.world.level.storage.loot.LootContextUser;
import net.mayaan.world.level.storage.loot.predicates.AllOfCondition;
import net.mayaan.world.level.storage.loot.predicates.AnyOfCondition;
import net.mayaan.world.level.storage.loot.predicates.InvertedLootItemCondition;

public interface LootItemCondition
extends LootContextUser,
Predicate<LootContext> {
    public static final Codec<LootItemCondition> TYPED_CODEC = BuiltInRegistries.LOOT_CONDITION_TYPE.byNameCodec().dispatch("condition", LootItemCondition::codec, c -> c);
    public static final Codec<LootItemCondition> DIRECT_CODEC = Codec.lazyInitialized(() -> Codec.withAlternative(TYPED_CODEC, AllOfCondition.INLINE_CODEC));
    public static final Codec<Holder<LootItemCondition>> CODEC = RegistryFileCodec.create(Registries.PREDICATE, DIRECT_CODEC);

    public MapCodec<? extends LootItemCondition> codec();

    @FunctionalInterface
    public static interface Builder {
        public LootItemCondition build();

        default public Builder invert() {
            return InvertedLootItemCondition.invert(this);
        }

        default public AnyOfCondition.Builder or(Builder other) {
            return AnyOfCondition.anyOf(this, other);
        }

        default public AllOfCondition.Builder and(Builder other) {
            return AllOfCondition.allOf(this, other);
        }
    }
}

