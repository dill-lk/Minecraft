/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.MoreObjects
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableSortedMap
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.Decoder
 *  com.mojang.serialization.Encoder
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.MapDecoder
 *  com.mojang.serialization.MapEncoder
 *  it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block.state;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.mayaan.world.level.block.state.StateHolder;
import net.mayaan.world.level.block.state.properties.Property;
import org.jspecify.annotations.Nullable;

public class StateDefinition<O, S extends StateHolder<O, S>> {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
    private final O owner;
    private final ImmutableSortedMap<String, Property<?>> propertiesByName;
    private final ImmutableList<S> states;

    protected StateDefinition(Function<O, S> defaultState, O owner, Factory<O, S> factory, Map<String, Property<?>> properties) {
        this.owner = owner;
        this.propertiesByName = ImmutableSortedMap.copyOf(properties);
        Supplier<StateHolder> defaultSupplier = () -> (StateHolder)defaultState.apply(owner);
        MapCodec<StateHolder> codec = MapCodec.of((MapEncoder)Encoder.empty(), (MapDecoder)Decoder.unit(defaultSupplier));
        for (Map.Entry entry : this.propertiesByName.entrySet()) {
            codec = StateDefinition.appendPropertyCodec(codec, defaultSupplier, (String)entry.getKey(), (Property)entry.getValue());
        }
        MapCodec<StateHolder> propertiesCodec = codec;
        LinkedHashMap statesByValues = Maps.newLinkedHashMap();
        ArrayList states = Lists.newArrayList();
        Stream<List<List<Object>>> stream = Stream.of(Collections.emptyList());
        for (Property property : this.propertiesByName.values()) {
            stream = stream.flatMap(list -> property.getPossibleValues().stream().map(value -> {
                ArrayList newList = Lists.newArrayList((Iterable)list);
                newList.add(Pair.of((Object)property, (Object)value));
                return newList;
            }));
        }
        stream.forEach(list -> {
            Reference2ObjectArrayMap map = new Reference2ObjectArrayMap(list.size());
            for (Pair pair : list) {
                map.put((Object)((Property)pair.getFirst()), (Object)((Comparable)pair.getSecond()));
            }
            StateHolder blockState = (StateHolder)factory.create(owner, map, propertiesCodec);
            statesByValues.put(map, blockState);
            states.add(blockState);
        });
        for (StateHolder blockState : states) {
            blockState.populateNeighbours(statesByValues);
        }
        this.states = ImmutableList.copyOf((Collection)states);
    }

    private static <S extends StateHolder<?, S>, T extends Comparable<T>> MapCodec<S> appendPropertyCodec(MapCodec<S> codec, Supplier<S> defaultSupplier, String name, Property<T> property) {
        return Codec.mapPair(codec, (MapCodec)property.valueCodec().fieldOf(name).orElseGet(e -> {}, () -> property.value((StateHolder)defaultSupplier.get()))).xmap(pair -> (StateHolder)((StateHolder)pair.getFirst()).setValue(property, ((Property.Value)pair.getSecond()).value()), state -> Pair.of((Object)state, property.value((StateHolder<?, ?>)state)));
    }

    public ImmutableList<S> getPossibleStates() {
        return this.states;
    }

    public S any() {
        return (S)((StateHolder)this.states.get(0));
    }

    public O getOwner() {
        return this.owner;
    }

    public Collection<Property<?>> getProperties() {
        return this.propertiesByName.values();
    }

    public String toString() {
        return MoreObjects.toStringHelper((Object)this).add("block", this.owner).add("properties", this.propertiesByName.values().stream().map(Property::getName).collect(Collectors.toList())).toString();
    }

    public @Nullable Property<?> getProperty(String name) {
        return (Property)this.propertiesByName.get((Object)name);
    }

    public static interface Factory<O, S> {
        public S create(O var1, Reference2ObjectArrayMap<Property<?>, Comparable<?>> var2, MapCodec<S> var3);
    }

    public static class Builder<O, S extends StateHolder<O, S>> {
        private final O owner;
        private final Map<String, Property<?>> properties = Maps.newHashMap();

        public Builder(O owner) {
            this.owner = owner;
        }

        public Builder<O, S> add(Property<?> ... properties) {
            for (Property<?> property : properties) {
                this.validateProperty(property);
                this.properties.put(property.getName(), property);
            }
            return this;
        }

        private <T extends Comparable<T>> void validateProperty(Property<T> property) {
            String name = property.getName();
            if (!NAME_PATTERN.matcher(name).matches()) {
                throw new IllegalArgumentException(String.valueOf(this.owner) + " has invalidly named property: " + name);
            }
            List<T> values = property.getPossibleValues();
            if (values.size() <= 1) {
                throw new IllegalArgumentException(String.valueOf(this.owner) + " attempted use property " + name + " with <= 1 possible values");
            }
            for (Comparable comparable : values) {
                String valueName = property.getName(comparable);
                if (NAME_PATTERN.matcher(valueName).matches()) continue;
                throw new IllegalArgumentException(String.valueOf(this.owner) + " has property: " + name + " with invalidly named value: " + valueName);
            }
            if (this.properties.containsKey(name)) {
                throw new IllegalArgumentException(String.valueOf(this.owner) + " has duplicate property: " + name);
            }
        }

        public StateDefinition<O, S> create(Function<O, S> defaultState, Factory<O, S> factory) {
            return new StateDefinition<O, S>(defaultState, this.owner, factory, this.properties);
        }
    }
}

