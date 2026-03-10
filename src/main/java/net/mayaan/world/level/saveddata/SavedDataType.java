/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.saveddata;

import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import net.mayaan.resources.Identifier;
import net.mayaan.util.datafix.DataFixTypes;
import net.mayaan.world.level.saveddata.SavedData;

public record SavedDataType<T extends SavedData>(Identifier id, Supplier<T> constructor, Codec<T> codec, DataFixTypes dataFixType) {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SavedDataType)) return false;
        SavedDataType type = (SavedDataType)obj;
        if (!this.id.equals(type.id)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return "SavedDataType[" + String.valueOf(this.id) + "]";
    }
}

