/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.level.ChunkPos;

public record ClientboundForgetLevelChunkPacket(ChunkPos pos) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundForgetLevelChunkPacket> STREAM_CODEC = Packet.codec(ClientboundForgetLevelChunkPacket::write, ClientboundForgetLevelChunkPacket::new);

    private ClientboundForgetLevelChunkPacket(FriendlyByteBuf input) {
        this(input.readChunkPos());
    }

    private void write(FriendlyByteBuf output) {
        output.writeChunkPos(this.pos);
    }

    @Override
    public PacketType<ClientboundForgetLevelChunkPacket> type() {
        return GamePacketTypes.CLIENTBOUND_FORGET_LEVEL_CHUNK;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleForgetLevelChunk(this);
    }
}

