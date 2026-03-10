/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.mayaan.nbt.NbtAccounter;
import net.mayaan.nbt.NumericTag;
import net.mayaan.nbt.StreamTagVisitor;
import net.mayaan.nbt.StringTagVisitor;
import net.mayaan.nbt.TagType;
import net.mayaan.nbt.TagVisitor;

public record ByteTag(byte value) implements NumericTag
{
    private static final int SELF_SIZE_IN_BYTES = 9;
    public static final TagType<ByteTag> TYPE = new TagType.StaticSize<ByteTag>(){

        @Override
        public ByteTag load(DataInput input, NbtAccounter accounter) throws IOException {
            return ByteTag.valueOf(readAccounted(input, accounter));
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
            return output.visit(readAccounted(input, accounter));
        }

        private static byte readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
            accounter.accountBytes(9L);
            return input.readByte();
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String getName() {
            return "BYTE";
        }

        @Override
        public String getPrettyName() {
            return "TAG_Byte";
        }
    };
    public static final ByteTag ZERO = ByteTag.valueOf((byte)0);
    public static final ByteTag ONE = ByteTag.valueOf((byte)1);

    public static ByteTag valueOf(byte data) {
        return Cache.cache[128 + data];
    }

    public static ByteTag valueOf(boolean data) {
        return data ? ONE : ZERO;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeByte(this.value);
    }

    @Override
    public int sizeInBytes() {
        return 9;
    }

    @Override
    public byte getId() {
        return 1;
    }

    public TagType<ByteTag> getType() {
        return TYPE;
    }

    @Override
    public ByteTag copy() {
        return this;
    }

    @Override
    public void accept(TagVisitor visitor) {
        visitor.visitByte(this);
    }

    @Override
    public long longValue() {
        return this.value;
    }

    @Override
    public int intValue() {
        return this.value;
    }

    @Override
    public short shortValue() {
        return this.value;
    }

    @Override
    public byte byteValue() {
        return this.value;
    }

    @Override
    public double doubleValue() {
        return this.value;
    }

    @Override
    public float floatValue() {
        return this.value;
    }

    @Override
    public Number box() {
        return this.value;
    }

    @Override
    public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
        return visitor.visit(this.value);
    }

    @Override
    public String toString() {
        StringTagVisitor visitor = new StringTagVisitor();
        visitor.visitByte(this);
        return visitor.build();
    }

    private static class Cache {
        private static final ByteTag[] cache = new ByteTag[256];

        private Cache() {
        }

        static {
            for (int i = 0; i < cache.length; ++i) {
                Cache.cache[i] = new ByteTag((byte)(i - 128));
            }
        }
    }
}

