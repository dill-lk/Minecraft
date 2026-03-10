/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.configuration;

import io.netty.buffer.ByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.configuration.ClientConfigurationPacketListener;
import net.mayaan.network.protocol.configuration.ConfigurationPacketTypes;

public record ClientboundCodeOfConductPacket(String codeOfConduct) implements Packet<ClientConfigurationPacketListener>
{
    public static final StreamCodec<ByteBuf, ClientboundCodeOfConductPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ClientboundCodeOfConductPacket::codeOfConduct, ClientboundCodeOfConductPacket::new);

    @Override
    public PacketType<ClientboundCodeOfConductPacket> type() {
        return ConfigurationPacketTypes.CLIENTBOUND_CODE_OF_CONDUCT;
    }

    @Override
    public void handle(ClientConfigurationPacketListener listener) {
        listener.handleCodeOfConduct(this);
    }
}

