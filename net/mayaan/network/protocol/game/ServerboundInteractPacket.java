/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.game;

import io.netty.buffer.ByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.phys.Vec3;

public record ServerboundInteractPacket(int entityId, InteractionHand hand, Vec3 location, boolean usingSecondaryAction) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<ByteBuf, ServerboundInteractPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ServerboundInteractPacket::entityId, InteractionHand.STREAM_CODEC, ServerboundInteractPacket::hand, Vec3.LP_STREAM_CODEC, ServerboundInteractPacket::location, ByteBufCodecs.BOOL, ServerboundInteractPacket::usingSecondaryAction, ServerboundInteractPacket::new);

    @Override
    public PacketType<ServerboundInteractPacket> type() {
        return GamePacketTypes.SERVERBOUND_INTERACT;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleInteract(this);
    }
}

