/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;

public record ServerboundContainerButtonClickPacket(int containerId, int buttonId) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundContainerButtonClickPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ServerboundContainerButtonClickPacket::containerId, ByteBufCodecs.VAR_INT, ServerboundContainerButtonClickPacket::buttonId, ServerboundContainerButtonClickPacket::new);

    @Override
    public PacketType<ServerboundContainerButtonClickPacket> type() {
        return GamePacketTypes.SERVERBOUND_CONTAINER_BUTTON_CLICK;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleContainerButtonClick(this);
    }
}

