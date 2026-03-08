/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public record ClientboundSetSubtitleTextPacket(Component text) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetSubtitleTextPacket> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_STREAM_CODEC, ClientboundSetSubtitleTextPacket::text, ClientboundSetSubtitleTextPacket::new);

    @Override
    public PacketType<ClientboundSetSubtitleTextPacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_SUBTITLE_TEXT;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.setSubtitleText(this);
    }
}

