/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;

public record ServerboundChatAckPacket(int offset) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundChatAckPacket> STREAM_CODEC = Packet.codec(ServerboundChatAckPacket::write, ServerboundChatAckPacket::new);

    private ServerboundChatAckPacket(FriendlyByteBuf input) {
        this(input.readVarInt());
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.offset);
    }

    @Override
    public PacketType<ServerboundChatAckPacket> type() {
        return GamePacketTypes.SERVERBOUND_CHAT_ACK;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleChatAck(this);
    }
}

