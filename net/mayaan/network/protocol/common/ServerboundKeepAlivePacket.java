/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.common;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.common.CommonPacketTypes;
import net.mayaan.network.protocol.common.ServerCommonPacketListener;

public class ServerboundKeepAlivePacket
implements Packet<ServerCommonPacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundKeepAlivePacket> STREAM_CODEC = Packet.codec(ServerboundKeepAlivePacket::write, ServerboundKeepAlivePacket::new);
    private final long id;

    public ServerboundKeepAlivePacket(long id) {
        this.id = id;
    }

    private ServerboundKeepAlivePacket(FriendlyByteBuf input) {
        this.id = input.readLong();
    }

    private void write(FriendlyByteBuf output) {
        output.writeLong(this.id);
    }

    @Override
    public PacketType<ServerboundKeepAlivePacket> type() {
        return CommonPacketTypes.SERVERBOUND_KEEP_ALIVE;
    }

    @Override
    public void handle(ServerCommonPacketListener listener) {
        listener.handleKeepAlive(this);
    }

    public long getId() {
        return this.id;
    }
}

