/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.configuration;

import io.netty.buffer.ByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.configuration.ClientConfigurationPacketListener;
import net.mayaan.network.protocol.configuration.ConfigurationPacketTypes;

public class ClientboundResetChatPacket
implements Packet<ClientConfigurationPacketListener> {
    public static final ClientboundResetChatPacket INSTANCE = new ClientboundResetChatPacket();
    public static final StreamCodec<ByteBuf, ClientboundResetChatPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private ClientboundResetChatPacket() {
    }

    @Override
    public PacketType<ClientboundResetChatPacket> type() {
        return ConfigurationPacketTypes.CLIENTBOUND_RESET_CHAT;
    }

    @Override
    public void handle(ClientConfigurationPacketListener listener) {
        listener.handleResetChat(this);
    }
}

