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

public class ClientboundSetChunkCacheCenterPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundSetChunkCacheCenterPacket> STREAM_CODEC = Packet.codec(ClientboundSetChunkCacheCenterPacket::write, ClientboundSetChunkCacheCenterPacket::new);
    private final int x;
    private final int z;

    public ClientboundSetChunkCacheCenterPacket(int x, int z) {
        this.x = x;
        this.z = z;
    }

    private ClientboundSetChunkCacheCenterPacket(FriendlyByteBuf input) {
        this.x = input.readVarInt();
        this.z = input.readVarInt();
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.x);
        output.writeVarInt(this.z);
    }

    @Override
    public PacketType<ClientboundSetChunkCacheCenterPacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_CHUNK_CACHE_CENTER;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetChunkCacheCenter(this);
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }
}

