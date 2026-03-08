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

public interface TagVisitor {
    public void visitString(StringTag var1);

    public void visitByte(ByteTag var1);

    public void visitShort(ShortTag var1);

    public void visitInt(IntTag var1);

    public void visitLong(LongTag var1);

    public void visitFloat(FloatTag var1);

    public void visitDouble(DoubleTag var1);

    public void visitByteArray(ByteArrayTag var1);

    public void visitIntArray(IntArrayTag var1);

    public void visitLongArray(LongArrayTag var1);

    public void visitList(ListTag var1);

    public void visitCompound(CompoundTag var1);

    public void visitEnd(EndTag var1);
}

