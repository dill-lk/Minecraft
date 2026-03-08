/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.util.debug.DebugSubscription;
import net.mayaan.world.level.ChunkPos;

public record ClientboundDebugChunkValuePacket(ChunkPos chunkPos, DebugSubscription.Update<?> update) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDebugChunkValuePacket> STREAM_CODEC = StreamCodec.composite(ChunkPos.STREAM_CODEC, ClientboundDebugChunkValuePacket::chunkPos, DebugSubscription.Update.STREAM_CODEC, ClientboundDebugChunkValuePacket::update, ClientboundDebugChunkValuePacket::new);

    @Override
    public PacketType<ClientboundDebugChunkValuePacket> type() {
        return GamePacketTypes.CLIENTBOUND_DEBUG_CHUNK_VALUE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleDebugChunkValue(this);
    }
}

