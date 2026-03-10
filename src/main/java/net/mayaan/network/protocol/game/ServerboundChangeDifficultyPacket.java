/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.game;

import io.netty.buffer.ByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.world.Difficulty;

public record ServerboundChangeDifficultyPacket(Difficulty difficulty) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<ByteBuf, ServerboundChangeDifficultyPacket> STREAM_CODEC = StreamCodec.composite(Difficulty.STREAM_CODEC, ServerboundChangeDifficultyPacket::difficulty, ServerboundChangeDifficultyPacket::new);

    @Override
    public PacketType<ServerboundChangeDifficultyPacket> type() {
        return GamePacketTypes.SERVERBOUND_CHANGE_DIFFICULTY;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleChangeDifficulty(this);
    }
}

