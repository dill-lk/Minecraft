/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 *  com.google.common.collect.Lists
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  io.netty.buffer.ByteBuf
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.util.random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.util.RandomSource;
import net.mayaan.util.random.Weighted;
import net.mayaan.util.random.WeightedRandom;
import org.jspecify.annotations.Nullable;

public final class WeightedList<E> {
    private static final int FLAT_THRESHOLD = 64;
    private final int totalWeight;
    private final List<Weighted<E>> items;
    private final @Nullable Selector<E> selector;

    private WeightedList(List<? extends Weighted<E>> items) {
        this.items = List.copyOf(items);
        this.totalWeight = WeightedRandom.getTotalWeight(items, Weighted::weight);
        this.selector = this.totalWeight == 0 ? null : (this.totalWeight < 64 ? new Flat<E>(this.items, this.totalWeight) : new Compact<E>(this.items));
    }

    public static <E> WeightedList<E> of() {
        return new WeightedList<E>(List.of());
    }

    public static <E> WeightedList<E> of(E value) {
        return new WeightedList<E>(List.of(new Weighted<E>(value, 1)));
    }

    @SafeVarargs
    public static <E> WeightedList<E> of(Weighted<E> ... items) {
        return new WeightedList<E>(List.of(items));
    }

    public static <E> WeightedList<E> of(List<Weighted<E>> items) {
        return new WeightedList<E>(items);
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public <T> WeightedList<T> map(Function<E, T> mapper) {
        return new WeightedList<E>(Lists.transform(this.items, e -> e.map(mapper)));
    }

    public Optional<E> getRandom(RandomSource random) {
        if (this.selector == null) {
            return Optional.empty();
        }
        int selection = random.nextInt(this.totalWeight);
        return Optional.of(this.selector.get(selection));
    }

    public E getRandomOrThrow(RandomSource random) {
        if (this.selector == null) {
            throw new IllegalStateException("Weighted list has no elements");
        }
        int selection = random.nextInt(this.totalWeight);
        return this.selector.get(selection);
    }

    public List<Weighted<E>> unwrap() {
        return this.items;
    }

    public static <E> Codec<WeightedList<E>> codec(Codec<E> elementCodec) {
        return Weighted.codec(elementCodec).listOf().xmap(WeightedList::of, WeightedList::unwrap);
    }

    public static <E> Codec<WeightedList<E>> codec(MapCodec<E> elementCodec) {
        return Weighted.codec(elementCodec).listOf().xmap(WeightedList::of, WeightedList::unwrap);
    }

    public static <E> Codec<WeightedList<E>> nonEmptyCodec(Codec<E> elementCodec) {
        return ExtraCodecs.nonEmptyList(Weighted.codec(elementCodec).listOf()).xmap(WeightedList::of, WeightedList::unwrap);
    }

    public static <E> Codec<WeightedList<E>> nonEmptyCodec(MapCodec<E> elementCodec) {
        return ExtraCodecs.nonEmptyList(Weighted.codec(elementCodec).listOf()).xmap(WeightedList::of, WeightedList::unwrap);
    }

    public static <E, B extends ByteBuf> StreamCodec<B, WeightedList<E>> streamCodec(StreamCodec<B, E> elementCodec) {
        return Weighted.streamCodec(elementCodec).apply(ByteBufCodecs.list()).map(WeightedList::of, WeightedList::unwrap);
    }

    public boolean contains(E value) {
        for (Weighted<E> item : this.items) {
            if (!item.value().equals(value)) continue;
            return true;
        }
        return false;
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WeightedList) {
            WeightedList list = (WeightedList)obj;
            return this.totalWeight == list.totalWeight && Objects.equals(this.items, list.items);
        }
        return false;
    }

    public int hashCode() {
        int result = this.totalWeight;
        result = 31 * result + this.items.hashCode();
        return result;
    }

    private static interface Selector<E> {
        public E get(int var1);
    }

    private static class Flat<E>
    implements Selector<E> {
        private final Object[] entries;

        private Flat(List<Weighted<E>> entries, int totalWeight) {
            this.entries = new Object[totalWeight];
            int i = 0;
            for (Weighted<E> entry : entries) {
                int weight = entry.weight();
                Arrays.fill(this.entries, i, i + weight, entry.value());
                i += weight;
            }
        }

        @Override
        public E get(int selection) {
            return (E)this.entries[selection];
        }
    }

    private static class Compact<E>
    implements Selector<E> {
        private final Weighted<?>[] entries;

        private Compact(List<Weighted<E>> entries) {
            this.entries = (Weighted[])entries.toArray(Weighted[]::new);
        }

        @Override
        public E get(int selection) {
            for (Weighted<?> entry : this.entries) {
                if ((selection -= entry.weight()) >= 0) continue;
                return (E)entry.value();
            }
            throw new IllegalStateException(selection + " exceeded total weight");
        }
    }

    public static class Builder<E> {
        private final ImmutableList.Builder<Weighted<E>> result = ImmutableList.builder();

        public Builder<E> add(E item) {
            return this.add(item, 1);
        }

        public Builder<E> add(E item, int weight) {
            this.result.add(new Weighted<E>(item, weight));
            return this;
        }

        public WeightedList<E> build() {
            return new WeightedList(this.result.build());
        }
    }
}

