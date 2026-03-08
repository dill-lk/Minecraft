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

public class ClientboundPingPacket
implements Packet<ClientCommonPacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundPingPacket> STREAM_CODEC = Packet.codec(ClientboundPingPacket::write, ClientboundPingPacket::new);
    private final int id;

    public ClientboundPingPacket(int id) {
        this.id = id;
    }

    private ClientboundPingPacket(FriendlyByteBuf input) {
        this.id = input.readInt();
    }

    private void write(FriendlyByteBuf output) {
        output.writeInt(this.id);
    }

    @Override
    public PacketType<ClientboundPingPacket> type() {
        return CommonPacketTypes.CLIENTBOUND_PING;
    }

    @Override
    public void handle(ClientCommonPacketListener listener) {
        listener.handlePing(this);
    }

    public int getId() {
        return this.id;
    }
}

