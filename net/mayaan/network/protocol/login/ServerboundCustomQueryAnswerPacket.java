/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.login;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.login.LoginPacketTypes;
import net.mayaan.network.protocol.login.ServerLoginPacketListener;
import net.mayaan.network.protocol.login.custom.CustomQueryAnswerPayload;
import net.mayaan.network.protocol.login.custom.DiscardedQueryAnswerPayload;
import org.jspecify.annotations.Nullable;

public record ServerboundCustomQueryAnswerPacket(int transactionId, @Nullable CustomQueryAnswerPayload payload) implements Packet<ServerLoginPacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundCustomQueryAnswerPacket> STREAM_CODEC = Packet.codec(ServerboundCustomQueryAnswerPacket::write, ServerboundCustomQueryAnswerPacket::read);
    private static final int MAX_PAYLOAD_SIZE = 0x100000;

    private static ServerboundCustomQueryAnswerPacket read(FriendlyByteBuf input) {
        int transactionId = input.readVarInt();
        return new ServerboundCustomQueryAnswerPacket(transactionId, ServerboundCustomQueryAnswerPacket.readPayload(transactionId, input));
    }

    private static CustomQueryAnswerPayload readPayload(int transactionId, FriendlyByteBuf input) {
        return ServerboundCustomQueryAnswerPacket.readUnknownPayload(input);
    }

    private static CustomQueryAnswerPayload readUnknownPayload(FriendlyByteBuf input) {
        int length = input.readableBytes();
        if (length < 0 || length > 0x100000) {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
        input.skipBytes(length);
        return DiscardedQueryAnswerPayload.INSTANCE;
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.transactionId);
        output.writeNullable(this.payload, (buf, data) -> data.write((FriendlyByteBuf)((Object)buf)));
    }

    @Override
    public PacketType<ServerboundCustomQueryAnswerPacket> type() {
        return LoginPacketTypes.SERVERBOUND_CUSTOM_QUERY_ANSWER;
    }

    @Override
    public void handle(ServerLoginPacketListener listener) {
        listener.handleCustomQueryPacket(this);
    }
}

