/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.mayaan.network.codec.StreamCodec;

public enum Unit {
    INSTANCE;

    public static final Codec<Unit> CODEC;
    public static final StreamCodec<ByteBuf, Unit> STREAM_CODEC;

    static {
        CODEC = MapCodec.unitCodec((Object)((Object)INSTANCE));
        STREAM_CODEC = StreamCodec.unit(INSTANCE);
    }
}

