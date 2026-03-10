/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.chat.MessageSignature;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public record ClientboundDeleteChatPacket(MessageSignature.Packed messageSignature) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundDeleteChatPacket> STREAM_CODEC = Packet.codec(ClientboundDeleteChatPacket::write, ClientboundDeleteChatPacket::new);

    private ClientboundDeleteChatPacket(FriendlyByteBuf input) {
        this(MessageSignature.Packed.read(input));
    }

    private void write(FriendlyByteBuf output) {
        MessageSignature.Packed.write(output, this.messageSignature);
    }

    @Override
    public PacketType<ClientboundDeleteChatPacket> type() {
        return GamePacketTypes.CLIENTBOUND_DELETE_CHAT;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleDeleteChat(this);
    }
}

