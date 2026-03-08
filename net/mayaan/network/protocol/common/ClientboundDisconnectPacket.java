/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.common;

import io.netty.buffer.ByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.common.ClientCommonPacketListener;
import net.mayaan.network.protocol.common.CommonPacketTypes;

public record ClientboundDisconnectPacket(Component reason) implements Packet<ClientCommonPacketListener>
{
    public static final StreamCodec<ByteBuf, ClientboundDisconnectPacket> STREAM_CODEC = ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.map(ClientboundDisconnectPacket::new, ClientboundDisconnectPacket::reason);

    @Override
    public PacketType<ClientboundDisconnectPacket> type() {
        return CommonPacketTypes.CLIENTBOUND_DISCONNECT;
    }

    @Override
    public void handle(ClientCommonPacketListener listener) {
        listener.handleDisconnect(this);
    }
}

