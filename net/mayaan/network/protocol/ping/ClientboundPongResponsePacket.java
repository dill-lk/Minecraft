/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.ping;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.ping.ClientPongPacketListener;
import net.mayaan.network.protocol.ping.PingPacketTypes;

public record ClientboundPongResponsePacket(long time) implements Packet<ClientPongPacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundPongResponsePacket> STREAM_CODEC = Packet.codec(ClientboundPongResponsePacket::write, ClientboundPongResponsePacket::new);

    private ClientboundPongResponsePacket(FriendlyByteBuf input) {
        this(input.readLong());
    }

    private void write(FriendlyByteBuf output) {
        output.writeLong(this.time);
    }

    @Override
    public PacketType<ClientboundPongResponsePacket> type() {
        return PingPacketTypes.CLIENTBOUND_PONG_RESPONSE;
    }

    @Override
    public void handle(ClientPongPacketListener listener) {
        listener.handlePongResponse(this);
    }
}

