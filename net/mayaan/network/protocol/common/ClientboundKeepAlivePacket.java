/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.common;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.common.ClientCommonPacketListener;
import net.mayaan.network.protocol.common.CommonPacketTypes;

public class ClientboundKeepAlivePacket
implements Packet<ClientCommonPacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundKeepAlivePacket> STREAM_CODEC = Packet.codec(ClientboundKeepAlivePacket::write, ClientboundKeepAlivePacket::new);
    private final long id;

    public ClientboundKeepAlivePacket(long id) {
        this.id = id;
    }

    private ClientboundKeepAlivePacket(FriendlyByteBuf input) {
        this.id = input.readLong();
    }

    private void write(FriendlyByteBuf output) {
        output.writeLong(this.id);
    }

    @Override
    public PacketType<ClientboundKeepAlivePacket> type() {
        return CommonPacketTypes.CLIENTBOUND_KEEP_ALIVE;
    }

    @Override
    public void handle(ClientCommonPacketListener listener) {
        listener.handleKeepAlive(this);
    }

    public long getId() {
        return this.id;
    }
}

