/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.chat.RemoteChatSession;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;

public record ServerboundChatSessionUpdatePacket(RemoteChatSession.Data chatSession) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundChatSessionUpdatePacket> STREAM_CODEC = Packet.codec(ServerboundChatSessionUpdatePacket::write, ServerboundChatSessionUpdatePacket::new);

    private ServerboundChatSessionUpdatePacket(FriendlyByteBuf input) {
        this(RemoteChatSession.Data.read(input));
    }

    private void write(FriendlyByteBuf output) {
        RemoteChatSession.Data.write(output, this.chatSession);
    }

    @Override
    public PacketType<ServerboundChatSessionUpdatePacket> type() {
        return GamePacketTypes.SERVERBOUND_CHAT_SESSION_UPDATE;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleChatSessionUpdate(this);
    }
}

