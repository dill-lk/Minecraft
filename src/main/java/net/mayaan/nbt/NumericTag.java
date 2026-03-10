/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.nbt;

import java.util.Optional;
import net.mayaan.nbt.ByteTag;
import net.mayaan.nbt.DoubleTag;
import net.mayaan.nbt.FloatTag;
import net.mayaan.nbt.IntTag;
import net.mayaan.nbt.LongTag;
import net.mayaan.nbt.PrimitiveTag;
import net.mayaan.nbt.ShortTag;

public sealed interface NumericTag
extends PrimitiveTag
permits ByteTag, ShortTag, IntTag, LongTag, FloatTag, DoubleTag {
    public byte byteValue();

    public short shortValue();

    public int intValue();

    public long longValue();

    public float floatValue();

    public double doubleValue();

    public Number box();

    @Override
    default public Optional<Number> asNumber() {
        return Optional.of(this.box());
    }

    @Override
    default public Optional<Byte> asByte() {
        return Optional.of(this.byteValue());
    }

    @Override
    default public Optional<Short> asShort() {
        return Optional.of(this.shortValue());
    }

    @Override
    default public Optional<Integer> asInt() {
        return Optional.of(this.intValue());
    }

    @Override
    default public Optional<Long> asLong() {
        return Optional.of(this.longValue());
    }

    @Override
    default public Optional<Float> asFloat() {
        return Optional.of(Float.valueOf(this.floatValue()));
    }

    @Override
    default public Optional<Double> asDouble() {
        return Optional.of(this.doubleValue());
    }

    @Override
    default public Optional<Boolean> asBoolean() {
        return Optional.of(this.byteValue() != 0);
    }
}

