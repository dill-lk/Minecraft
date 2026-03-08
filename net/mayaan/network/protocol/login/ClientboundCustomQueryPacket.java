/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.login;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.login.ClientLoginPacketListener;
import net.mayaan.network.protocol.login.LoginPacketTypes;
import net.mayaan.network.protocol.login.custom.CustomQueryPayload;
import net.mayaan.network.protocol.login.custom.DiscardedQueryPayload;
import net.mayaan.resources.Identifier;

public record ClientboundCustomQueryPacket(int transactionId, CustomQueryPayload payload) implements Packet<ClientLoginPacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundCustomQueryPacket> STREAM_CODEC = Packet.codec(ClientboundCustomQueryPacket::write, ClientboundCustomQueryPacket::new);
    private static final int MAX_PAYLOAD_SIZE = 0x100000;

    private ClientboundCustomQueryPacket(FriendlyByteBuf input) {
        this(input.readVarInt(), ClientboundCustomQueryPacket.readPayload(input.readIdentifier(), input));
    }

    private static CustomQueryPayload readPayload(Identifier identifier, FriendlyByteBuf input) {
        return ClientboundCustomQueryPacket.readUnknownPayload(identifier, input);
    }

    private static DiscardedQueryPayload readUnknownPayload(Identifier identifier, FriendlyByteBuf input) {
        int length = input.readableBytes();
        if (length < 0 || length > 0x100000) {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
        input.skipBytes(length);
        return new DiscardedQueryPayload(identifier);
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.transactionId);
        output.writeIdentifier(this.payload.id());
        this.payload.write(output);
    }

    @Override
    public PacketType<ClientboundCustomQueryPacket> type() {
        return LoginPacketTypes.CLIENTBOUND_CUSTOM_QUERY;
    }

    @Override
    public void handle(ClientLoginPacketListener listener) {
        listener.handleCustomQuery(this);
    }
}

