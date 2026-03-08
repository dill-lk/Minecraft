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
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public class ClientboundChunkBatchStartPacket
implements Packet<ClientGamePacketListener> {
    public static final ClientboundChunkBatchStartPacket INSTANCE = new ClientboundChunkBatchStartPacket();
    public static final StreamCodec<ByteBuf, ClientboundChunkBatchStartPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private ClientboundChunkBatchStartPacket() {
    }

    @Override
    public PacketType<ClientboundChunkBatchStartPacket> type() {
        return GamePacketTypes.CLIENTBOUND_CHUNK_BATCH_START;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleChunkBatchStart(this);
    }
}

