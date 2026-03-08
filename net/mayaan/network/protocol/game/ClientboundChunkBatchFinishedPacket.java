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

public record ClientboundChunkBatchFinishedPacket(int batchSize) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundChunkBatchFinishedPacket> STREAM_CODEC = Packet.codec(ClientboundChunkBatchFinishedPacket::write, ClientboundChunkBatchFinishedPacket::new);

    private ClientboundChunkBatchFinishedPacket(FriendlyByteBuf input) {
        this(input.readVarInt());
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.batchSize);
    }

    @Override
    public PacketType<ClientboundChunkBatchFinishedPacket> type() {
        return GamePacketTypes.CLIENTBOUND_CHUNK_BATCH_FINISHED;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleChunkBatchFinished(this);
    }
}

