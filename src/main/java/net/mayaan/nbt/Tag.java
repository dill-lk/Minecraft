/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.nbt;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Optional;
import net.mayaan.nbt.CollectionTag;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.nbt.EndTag;
import net.mayaan.nbt.ListTag;
import net.mayaan.nbt.PrimitiveTag;
import net.mayaan.nbt.StreamTagVisitor;
import net.mayaan.nbt.TagType;
import net.mayaan.nbt.TagVisitor;

public sealed interface Tag
permits CompoundTag, CollectionTag, PrimitiveTag, EndTag {
    public static final int OBJECT_HEADER = 8;
    public static final int ARRAY_HEADER = 12;
    public static final int OBJECT_REFERENCE = 4;
    public static final int STRING_SIZE = 28;
    public static final byte TAG_END = 0;
    public static final byte TAG_BYTE = 1;
    public static final byte TAG_SHORT = 2;
    public static final byte TAG_INT = 3;
    public static final byte TAG_LONG = 4;
    public static final byte TAG_FLOAT = 5;
    public static final byte TAG_DOUBLE = 6;
    public static final byte TAG_BYTE_ARRAY = 7;
    public static final byte TAG_STRING = 8;
    public static final byte TAG_LIST = 9;
    public static final byte TAG_COMPOUND = 10;
    public static final byte TAG_INT_ARRAY = 11;
    public static final byte TAG_LONG_ARRAY = 12;
    public static final int MAX_DEPTH = 512;

    public void write(DataOutput var1) throws IOException;

    public String toString();

    public byte getId();

    public TagType<?> getType();

    public Tag copy();

    public int sizeInBytes();

    public void accept(TagVisitor var1);

    public StreamTagVisitor.ValueResult accept(StreamTagVisitor var1);

    default public void acceptAsRoot(StreamTagVisitor output) {
        StreamTagVisitor.ValueResult entryResult = output.visitRootEntry(this.getType());
        if (entryResult == StreamTagVisitor.ValueResult.CONTINUE) {
            this.accept(output);
        }
    }

    default public Optional<String> asString() {
        return Optional.empty();
    }

    default public Optional<Number> asNumber() {
        return Optional.empty();
    }

    default public Optional<Byte> asByte() {
        return this.asNumber().map(Number::byteValue);
    }

    default public Optional<Short> asShort() {
        return this.asNumber().map(Number::shortValue);
    }

    default public Optional<Integer> asInt() {
        return this.asNumber().map(Number::intValue);
    }

    default public Optional<Long> asLong() {
        return this.asNumber().map(Number::longValue);
    }

    default public Optional<Float> asFloat() {
        return this.asNumber().map(Number::floatValue);
    }

    default public Optional<Double> asDouble() {
        return this.asNumber().map(Number::doubleValue);
    }

    default public Optional<Boolean> asBoolean() {
        return this.asByte().map(b -> b != 0);
    }

    default public Optional<byte[]> asByteArray() {
        return Optional.empty();
    }

    default public Optional<int[]> asIntArray() {
        return Optional.empty();
    }

    default public Optional<long[]> asLongArray() {
        return Optional.empty();
    }

    default public Optional<CompoundTag> asCompound() {
        return Optional.empty();
    }

    default public Optional<ListTag> asList() {
        return Optional.empty();
    }
}

