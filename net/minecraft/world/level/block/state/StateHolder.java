/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.world.level.block.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.Nullable;

public abstract class StateHolder<O, S> {
    public static final String NAME_TAG = "Name";
    public static final String PROPERTIES_TAG = "Properties";
    private static final Function<Map.Entry<Property<?>, Comparable<?>>, String> PROPERTY_ENTRY_TO_STRING_FUNCTION = new Function<Map.Entry<Property<?>, Comparable<?>>, String>(){

        @Override
        public String apply(@Nullable Map.Entry<Property<?>, Comparable<?>> entry) {
            if (entry == null) {
                return "<NULL>";
            }
            Property<?> property = entry.getKey();
            return property.getName() + "=" + this.getName(property, entry.getValue());
        }

        private <T extends Comparable<T>> String getName(Property<T> property, Comparable<?> value) {
            return property.getName(value);
        }
    };
    protected final O owner;
    private final Reference2ObjectArrayMap<Property<?>, Comparable<?>> values;
    private Map<Property<?>, S[]> neighbours;
    protected final MapCodec<S> propertiesCodec;

    protected StateHolder(O owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> values, MapCodec<S> propertiesCodec) {
        this.owner = owner;
        this.values = values;
        this.propertiesCodec = propertiesCodec;
    }

    public <T extends Comparable<T>> S cycle(Property<T> property) {
        return this.setValue(property, (Comparable)StateHolder.findNextInCollection(property.getPossibleValues(), this.getValue(property)));
    }

    protected static <T> T findNextInCollection(List<T> values, T current) {
        int nextIndex = values.indexOf(current) + 1;
        return (T)(nextIndex == values.size() ? values.getFirst() : values.get(nextIndex));
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.owner);
        if (!this.getValues().isEmpty()) {
            builder.append('[');
            builder.append(this.getValues().entrySet().stream().map(PROPERTY_ENTRY_TO_STRING_FUNCTION).collect(Collectors.joining(",")));
            builder.append(']');
        }
        return builder.toString();
    }

    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public Collection<Property<?>> getProperties() {
        return Collections.unmodifiableCollection(this.values.keySet());
    }

    public boolean hasProperty(Property<?> property) {
        return this.values.containsKey(property);
    }

    public <T extends Comparable<T>> T getValue(Property<T> property) {
        Comparable value = (Comparable)this.values.get(property);
        if (value == null) {
            throw new IllegalArgumentException("Cannot get property " + String.valueOf(property) + " as it does not exist in " + String.valueOf(this.owner));
        }
        return (T)((Comparable)property.getValueClass().cast(value));
    }

    public <T extends Comparable<T>> Optional<T> getOptionalValue(Property<T> property) {
        return Optional.ofNullable(this.getNullableValue(property));
    }

    public <T extends Comparable<T>> T getValueOrElse(Property<T> property, T defaultValue) {
        return (T)((Comparable)Objects.requireNonNullElse(this.getNullableValue(property), defaultValue));
    }

    private <T extends Comparable<T>> @Nullable T getNullableValue(Property<T> property) {
        Comparable value = (Comparable)this.values.get(property);
        if (value == null) {
            return null;
        }
        return (T)((Comparable)property.getValueClass().cast(value));
    }

    public <T extends Comparable<T>, V extends T> S setValue(Property<T> property, V value) {
        Comparable oldValue = (Comparable)this.values.get(property);
        if (oldValue == null) {
            throw new IllegalArgumentException("Cannot set property " + String.valueOf(property) + " as it does not exist in " + String.valueOf(this.owner));
        }
        return this.setValueInternal(property, value, oldValue);
    }

    public <T extends Comparable<T>, V extends T> S trySetValue(Property<T> property, V value) {
        Comparable oldValue = (Comparable)this.values.get(property);
        if (oldValue == null) {
            return (S)this;
        }
        return this.setValueInternal(property, value, oldValue);
    }

    private <T extends Comparable<T>, V extends T> S setValueInternal(Property<T> property, V value, Comparable<?> oldValue) {
        if (oldValue.equals(value)) {
            return (S)this;
        }
        int internalIndex = property.getInternalIndex(value);
        if (internalIndex < 0) {
            throw new IllegalArgumentException("Cannot set property " + String.valueOf(property) + " to " + String.valueOf(value) + " on " + String.valueOf(this.owner) + ", it is not an allowed value");
        }
        return this.neighbours.get(property)[internalIndex];
    }

    public void populateNeighbours(Map<Map<Property<?>, Comparable<?>>, S> statesByValues) {
        if (this.neighbours != null) {
            throw new IllegalStateException();
        }
        Reference2ObjectArrayMap neighbours = new Reference2ObjectArrayMap(this.values.size());
        for (Map.Entry entry : this.values.entrySet()) {
            Property property = (Property)entry.getKey();
            neighbours.put(property, property.getPossibleValues().stream().map(value -> statesByValues.get(this.makeNeighbourValues(property, (Comparable<?>)value))).toArray());
        }
        this.neighbours = neighbours;
    }

    private Map<Property<?>, Comparable<?>> makeNeighbourValues(Property<?> property, Comparable<?> value) {
        Reference2ObjectArrayMap neighbour = new Reference2ObjectArrayMap(this.values);
        neighbour.put(property, value);
        return neighbour;
    }

    public Map<Property<?>, Comparable<?>> getValues() {
        return this.values;
    }

    protected static <O, S extends StateHolder<O, S>> Codec<S> codec(Codec<O> ownerCodec, Function<O, S> defaultState) {
        return ownerCodec.dispatch(NAME_TAG, s -> s.owner, o -> {
            StateHolder defaultValue = (StateHolder)defaultState.apply(o);
            if (defaultValue.getValues().isEmpty()) {
                return MapCodec.unit((Object)defaultValue);
            }
            return defaultValue.propertiesCodec.codec().lenientOptionalFieldOf(PROPERTIES_TAG).xmap(oo -> oo.orElse(defaultValue), Optional::of);
        });
    }
}

