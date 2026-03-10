/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.network.chat.numbers;

import com.mojang.serialization.MapCodec;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.numbers.NumberFormat;
import net.mayaan.network.codec.StreamCodec;

public interface NumberFormatType<T extends NumberFormat> {
    public MapCodec<T> mapCodec();

    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
}

