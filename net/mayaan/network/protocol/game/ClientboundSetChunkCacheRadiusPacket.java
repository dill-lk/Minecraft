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

public class ClientboundSetChunkCacheRadiusPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundSetChunkCacheRadiusPacket> STREAM_CODEC = Packet.codec(ClientboundSetChunkCacheRadiusPacket::write, ClientboundSetChunkCacheRadiusPacket::new);
    private final int radius;

    public ClientboundSetChunkCacheRadiusPacket(int radius) {
        this.radius = radius;
    }

    private ClientboundSetChunkCacheRadiusPacket(FriendlyByteBuf input) {
        this.radius = input.readVarInt();
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.radius);
    }

    @Override
    public PacketType<ClientboundSetChunkCacheRadiusPacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_CHUNK_CACHE_RADIUS;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetChunkCacheRadius(this);
    }

    public int getRadius() {
        return this.radius;
    }
}

