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

public class ServerboundPongPacket
implements Packet<ServerCommonPacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundPongPacket> STREAM_CODEC = Packet.codec(ServerboundPongPacket::write, ServerboundPongPacket::new);
    private final int id;

    public ServerboundPongPacket(int id) {
        this.id = id;
    }

    private ServerboundPongPacket(FriendlyByteBuf input) {
        this.id = input.readInt();
    }

    private void write(FriendlyByteBuf output) {
        output.writeInt(this.id);
    }

    @Override
    public PacketType<ServerboundPongPacket> type() {
        return CommonPacketTypes.SERVERBOUND_PONG;
    }

    @Override
    public void handle(ServerCommonPacketListener listener) {
        listener.handlePong(this);
    }

    public int getId() {
        return this.id;
    }
}

