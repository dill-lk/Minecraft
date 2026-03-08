/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public record ClientboundPlayerRotationPacket(float yRot, boolean relativeY, float xRot, boolean relativeX) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundPlayerRotationPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, ClientboundPlayerRotationPacket::yRot, ByteBufCodecs.BOOL, ClientboundPlayerRotationPacket::relativeY, ByteBufCodecs.FLOAT, ClientboundPlayerRotationPacket::xRot, ByteBufCodecs.BOOL, ClientboundPlayerRotationPacket::relativeX, ClientboundPlayerRotationPacket::new);

    @Override
    public PacketType<ClientboundPlayerRotationPacket> type() {
        return GamePacketTypes.CLIENTBOUND_PLAYER_ROTATION;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleRotatePlayer(this);
    }
}

