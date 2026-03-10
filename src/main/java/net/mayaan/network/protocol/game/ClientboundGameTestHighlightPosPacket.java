/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.game;

import io.netty.buffer.ByteBuf;
import net.mayaan.core.BlockPos;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public record ClientboundGameTestHighlightPosPacket(BlockPos absolutePos, BlockPos relativePos) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<ByteBuf, ClientboundGameTestHighlightPosPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ClientboundGameTestHighlightPosPacket::absolutePos, BlockPos.STREAM_CODEC, ClientboundGameTestHighlightPosPacket::relativePos, ClientboundGameTestHighlightPosPacket::new);

    @Override
    public PacketType<ClientboundGameTestHighlightPosPacket> type() {
        return GamePacketTypes.CLIENTBOUND_GAME_TEST_HIGHLIGHT_POS;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleGameTestHighlightPos(this);
    }
}

