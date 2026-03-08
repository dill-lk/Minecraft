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

public record ServerboundPickItemFromEntityPacket(int id, boolean includeData) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<ByteBuf, ServerboundPickItemFromEntityPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ServerboundPickItemFromEntityPacket::id, ByteBufCodecs.BOOL, ServerboundPickItemFromEntityPacket::includeData, ServerboundPickItemFromEntityPacket::new);

    @Override
    public PacketType<ServerboundPickItemFromEntityPacket> type() {
        return GamePacketTypes.SERVERBOUND_PICK_ITEM_FROM_ENTITY;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handlePickItemFromEntity(this);
    }
}

