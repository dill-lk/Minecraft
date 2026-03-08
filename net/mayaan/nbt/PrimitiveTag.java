/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.nbt;

import net.mayaan.nbt.NumericTag;
import net.mayaan.nbt.StringTag;
import net.mayaan.nbt.Tag;

public sealed interface PrimitiveTag
extends Tag
permits NumericTag, StringTag {
    @Override
    default public Tag copy() {
        return this;
    }
}

