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
import net.mayaan.world.entity.Entity;
import net.mayaan.world.phys.Vec3;

public record ServerboundMoveVehiclePacket(Vec3 position, float yRot, float xRot, boolean onGround) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundMoveVehiclePacket> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, ServerboundMoveVehiclePacket::position, ByteBufCodecs.FLOAT, ServerboundMoveVehiclePacket::yRot, ByteBufCodecs.FLOAT, ServerboundMoveVehiclePacket::xRot, ByteBufCodecs.BOOL, ServerboundMoveVehiclePacket::onGround, ServerboundMoveVehiclePacket::new);

    public static ServerboundMoveVehiclePacket fromEntity(Entity entity) {
        if (entity.isInterpolating()) {
            return new ServerboundMoveVehiclePacket(entity.getInterpolation().position(), entity.getInterpolation().yRot(), entity.getInterpolation().xRot(), entity.onGround());
        }
        return new ServerboundMoveVehiclePacket(entity.position(), entity.getYRot(), entity.getXRot(), entity.onGround());
    }

    @Override
    public PacketType<ServerboundMoveVehiclePacket> type() {
        return GamePacketTypes.SERVERBOUND_MOVE_VEHICLE;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleMoveVehicle(this);
    }
}

