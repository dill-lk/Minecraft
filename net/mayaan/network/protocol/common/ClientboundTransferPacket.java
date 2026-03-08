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

public record ClientboundTransferPacket(String host, int port) implements Packet<ClientCommonPacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundTransferPacket> STREAM_CODEC = Packet.codec(ClientboundTransferPacket::write, ClientboundTransferPacket::new);

    private ClientboundTransferPacket(FriendlyByteBuf input) {
        this(input.readUtf(), input.readVarInt());
    }

    private void write(FriendlyByteBuf output) {
        output.writeUtf(this.host);
        output.writeVarInt(this.port);
    }

    @Override
    public PacketType<ClientboundTransferPacket> type() {
        return CommonPacketTypes.CLIENTBOUND_TRANSFER;
    }

    @Override
    public void handle(ClientCommonPacketListener listener) {
        listener.handleTransfer(this);
    }
}

