/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.core.BlockPos;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.world.level.block.state.properties.TestBlockMode;

public record ServerboundSetTestBlockPacket(BlockPos position, TestBlockMode mode, String message) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundSetTestBlockPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ServerboundSetTestBlockPacket::position, TestBlockMode.STREAM_CODEC, ServerboundSetTestBlockPacket::mode, ByteBufCodecs.STRING_UTF8, ServerboundSetTestBlockPacket::message, ServerboundSetTestBlockPacket::new);

    @Override
    public PacketType<ServerboundSetTestBlockPacket> type() {
        return GamePacketTypes.SERVERBOUND_SET_TEST_BLOCK;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleSetTestBlock(this);
    }
}

