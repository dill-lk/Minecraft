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

public record ServerboundChatCommandPacket(String command) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundChatCommandPacket> STREAM_CODEC = Packet.codec(ServerboundChatCommandPacket::write, ServerboundChatCommandPacket::new);

    private ServerboundChatCommandPacket(FriendlyByteBuf input) {
        this(input.readUtf());
    }

    private void write(FriendlyByteBuf output) {
        output.writeUtf(this.command);
    }

    @Override
    public PacketType<ServerboundChatCommandPacket> type() {
        return GamePacketTypes.SERVERBOUND_CHAT_COMMAND;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleChatCommand(this);
    }
}

