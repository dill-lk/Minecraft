/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol;

import io.netty.buffer.ByteBuf;
import net.mayaan.network.PacketListener;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.codec.StreamDecoder;
import net.mayaan.network.codec.StreamMemberEncoder;
import net.mayaan.network.protocol.PacketType;

public interface Packet<T extends PacketListener> {
    public PacketType<? extends Packet<T>> type();

    public void handle(T var1);

    default public boolean isSkippable() {
        return false;
    }

    default public boolean isTerminal() {
        return false;
    }

    public static <B extends ByteBuf, T extends Packet<?>> StreamCodec<B, T> codec(StreamMemberEncoder<B, T> writer, StreamDecoder<B, T> reader) {
        return StreamCodec.ofMember(writer, reader);
    }
}

