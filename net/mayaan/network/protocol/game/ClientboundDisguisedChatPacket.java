/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.ChatType;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public record ClientboundDisguisedChatPacket(Component message, ChatType.Bound chatType) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDisguisedChatPacket> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_STREAM_CODEC, ClientboundDisguisedChatPacket::message, ChatType.Bound.STREAM_CODEC, ClientboundDisguisedChatPacket::chatType, ClientboundDisguisedChatPacket::new);

    @Override
    public PacketType<ClientboundDisguisedChatPacket> type() {
        return GamePacketTypes.CLIENTBOUND_DISGUISED_CHAT;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleDisguisedChat(this);
    }

    @Override
    public boolean isSkippable() {
        return true;
    }
}

