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
import net.mayaan.world.entity.Entity;
import net.mayaan.world.phys.Vec3;

public record ClientboundMoveVehiclePacket(Vec3 position, float yRot, float xRot) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundMoveVehiclePacket> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, ClientboundMoveVehiclePacket::position, ByteBufCodecs.FLOAT, ClientboundMoveVehiclePacket::yRot, ByteBufCodecs.FLOAT, ClientboundMoveVehiclePacket::xRot, ClientboundMoveVehiclePacket::new);

    public static ClientboundMoveVehiclePacket fromEntity(Entity entity) {
        return new ClientboundMoveVehiclePacket(entity.position(), entity.getYRot(), entity.getXRot());
    }

    @Override
    public PacketType<ClientboundMoveVehiclePacket> type() {
        return GamePacketTypes.CLIENTBOUND_MOVE_VEHICLE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleMoveVehicle(this);
    }
}

