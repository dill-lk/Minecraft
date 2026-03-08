/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.nbt;

import net.mayaan.nbt.ByteArrayTag;
import net.mayaan.nbt.ByteTag;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.nbt.DoubleTag;
import net.mayaan.nbt.EndTag;
import net.mayaan.nbt.FloatTag;
import net.mayaan.nbt.IntArrayTag;
import net.mayaan.nbt.IntTag;
import net.mayaan.nbt.ListTag;
import net.mayaan.nbt.LongArrayTag;
import net.mayaan.nbt.LongTag;
import net.mayaan.nbt.ShortTag;
import net.mayaan.nbt.StringTag;
import net.mayaan.nbt.TagType;

public class TagTypes {
    private static final TagType<?>[] TYPES = new TagType[]{EndTag.TYPE, ByteTag.TYPE, ShortTag.TYPE, IntTag.TYPE, LongTag.TYPE, FloatTag.TYPE, DoubleTag.TYPE, ByteArrayTag.TYPE, StringTag.TYPE, ListTag.TYPE, CompoundTag.TYPE, IntArrayTag.TYPE, LongArrayTag.TYPE};

    public static TagType<?> getType(int typeId) {
        if (typeId < 0 || typeId >= TYPES.length) {
            return TagType.createInvalid(typeId);
        }
        return TYPES[typeId];
    }
}

