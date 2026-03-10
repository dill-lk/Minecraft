/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.advancements.criterion;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.Predicate;
import net.mayaan.advancements.criterion.DataComponentMatchers;
import net.mayaan.advancements.criterion.MinMaxBounds;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.HolderSet;
import net.mayaan.core.RegistryCodecs;
import net.mayaan.core.registries.Registries;
import net.mayaan.tags.TagKey;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemInstance;
import net.mayaan.world.level.ItemLike;

public record ItemPredicate(Optional<HolderSet<Item>> items, MinMaxBounds.Ints count, DataComponentMatchers components) implements Predicate<ItemInstance>
{
    public static final Codec<ItemPredicate> CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.ITEM).optionalFieldOf("items").forGetter(ItemPredicate::items), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("count", (Object)MinMaxBounds.Ints.ANY).forGetter(ItemPredicate::count), (App)DataComponentMatchers.CODEC.forGetter(ItemPredicate::components)).apply((Applicative)i, ItemPredicate::new));

    @Override
    public boolean test(ItemInstance itemStack) {
        if (this.items.isPresent() && !itemStack.is(this.items.get())) {
            return false;
        }
        if (!this.count.matches(itemStack.count())) {
            return false;
        }
        return this.components.test(itemStack);
    }

    public static class Builder {
        private Optional<HolderSet<Item>> items = Optional.empty();
        private MinMaxBounds.Ints count = MinMaxBounds.Ints.ANY;
        private DataComponentMatchers components = DataComponentMatchers.ANY;

        public static Builder item() {
            return new Builder();
        }

        public Builder of(HolderGetter<Item> lookup, ItemLike ... items) {
            this.items = Optional.of(HolderSet.direct(i -> i.asItem().builtInRegistryHolder(), items));
            return this;
        }

        public Builder of(HolderGetter<Item> lookup, TagKey<Item> tag) {
            this.items = Optional.of(lookup.getOrThrow(tag));
            return this;
        }

        public Builder withCount(MinMaxBounds.Ints count) {
            this.count = count;
            return this;
        }

        public Builder withComponents(DataComponentMatchers components) {
            this.components = components;
            return this;
        }

        public ItemPredicate build() {
            return new ItemPredicate(this.items, this.count, this.components);
        }
    }
}

