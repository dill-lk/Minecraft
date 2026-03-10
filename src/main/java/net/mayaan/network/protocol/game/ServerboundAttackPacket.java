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

public record ServerboundAttackPacket(int entityId) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<ByteBuf, ServerboundAttackPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ServerboundAttackPacket::entityId, ServerboundAttackPacket::new);

    @Override
    public PacketType<ServerboundAttackPacket> type() {
        return GamePacketTypes.SERVERBOUND_ATTACK;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleAttack(this);
    }
}

