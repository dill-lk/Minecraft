/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.game;

import io.netty.buffer.ByteBuf;
import net.mayaan.core.BlockPos;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;

public record ServerboundPickItemFromBlockPacket(BlockPos pos, boolean includeData) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<ByteBuf, ServerboundPickItemFromBlockPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ServerboundPickItemFromBlockPacket::pos, ByteBufCodecs.BOOL, ServerboundPickItemFromBlockPacket::includeData, ServerboundPickItemFromBlockPacket::new);

    @Override
    public PacketType<ServerboundPickItemFromBlockPacket> type() {
        return GamePacketTypes.SERVERBOUND_PICK_ITEM_FROM_BLOCK;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handlePickItemFromBlock(this);
    }
}

