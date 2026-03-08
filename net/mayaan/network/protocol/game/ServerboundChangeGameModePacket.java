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
import net.mayaan.world.level.GameType;

public record ServerboundChangeGameModePacket(GameType mode) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<ByteBuf, ServerboundChangeGameModePacket> STREAM_CODEC = StreamCodec.composite(GameType.STREAM_CODEC, ServerboundChangeGameModePacket::mode, ServerboundChangeGameModePacket::new);

    @Override
    public PacketType<ServerboundChangeGameModePacket> type() {
        return GamePacketTypes.SERVERBOUND_CHANGE_GAME_MODE;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleChangeGameMode(this);
    }
}

