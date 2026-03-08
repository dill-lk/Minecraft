/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.handshake;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.handshake.ClientIntent;
import net.mayaan.network.protocol.handshake.HandshakePacketTypes;
import net.mayaan.network.protocol.handshake.ServerHandshakePacketListener;

public record ClientIntentionPacket(int protocolVersion, String hostName, int port, ClientIntent intention) implements Packet<ServerHandshakePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientIntentionPacket> STREAM_CODEC = Packet.codec(ClientIntentionPacket::write, ClientIntentionPacket::new);
    private static final int MAX_HOST_LENGTH = 255;

    private ClientIntentionPacket(FriendlyByteBuf input) {
        this(input.readVarInt(), input.readUtf(255), input.readUnsignedShort(), ClientIntent.byId(input.readVarInt()));
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.protocolVersion);
        output.writeUtf(this.hostName);
        output.writeShort(this.port);
        output.writeVarInt(this.intention.id());
    }

    @Override
    public PacketType<ClientIntentionPacket> type() {
        return HandshakePacketTypes.CLIENT_INTENTION;
    }

    @Override
    public void handle(ServerHandshakePacketListener listener) {
        listener.handleIntention(this);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}

