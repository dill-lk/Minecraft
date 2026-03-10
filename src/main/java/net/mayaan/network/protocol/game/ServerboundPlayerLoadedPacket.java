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

public record ServerboundPlayerLoadedPacket() implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<ByteBuf, ServerboundPlayerLoadedPacket> STREAM_CODEC = StreamCodec.unit(new ServerboundPlayerLoadedPacket());

    @Override
    public PacketType<ServerboundPlayerLoadedPacket> type() {
        return GamePacketTypes.SERVERBOUND_PLAYER_LOADED;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleAcceptPlayerLoad(this);
    }
}

