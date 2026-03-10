/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.status;

import io.netty.buffer.ByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.status.ServerStatusPacketListener;
import net.mayaan.network.protocol.status.StatusPacketTypes;

public class ServerboundStatusRequestPacket
implements Packet<ServerStatusPacketListener> {
    public static final ServerboundStatusRequestPacket INSTANCE = new ServerboundStatusRequestPacket();
    public static final StreamCodec<ByteBuf, ServerboundStatusRequestPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private ServerboundStatusRequestPacket() {
    }

    @Override
    public PacketType<ServerboundStatusRequestPacket> type() {
        return StatusPacketTypes.SERVERBOUND_STATUS_REQUEST;
    }

    @Override
    public void handle(ServerStatusPacketListener listener) {
        listener.handleStatusRequest(this);
    }
}

