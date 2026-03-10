/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import java.util.Set;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.entity.PositionMoveRotation;
import net.mayaan.world.entity.Relative;

public record ClientboundTeleportEntityPacket(int id, PositionMoveRotation change, Set<Relative> relatives, boolean onGround) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundTeleportEntityPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundTeleportEntityPacket::id, PositionMoveRotation.STREAM_CODEC, ClientboundTeleportEntityPacket::change, Relative.SET_STREAM_CODEC, ClientboundTeleportEntityPacket::relatives, ByteBufCodecs.BOOL, ClientboundTeleportEntityPacket::onGround, ClientboundTeleportEntityPacket::new);

    public static ClientboundTeleportEntityPacket teleport(int id, PositionMoveRotation values, Set<Relative> relatives, boolean onGround) {
        return new ClientboundTeleportEntityPacket(id, values, relatives, onGround);
    }

    @Override
    public PacketType<ClientboundTeleportEntityPacket> type() {
        return GamePacketTypes.CLIENTBOUND_TELEPORT_ENTITY;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleTeleportEntity(this);
    }
}

