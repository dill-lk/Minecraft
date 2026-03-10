/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.common;

import io.netty.buffer.ByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.common.ClientCommonPacketListener;
import net.mayaan.network.protocol.common.CommonPacketTypes;

public class ClientboundClearDialogPacket
implements Packet<ClientCommonPacketListener> {
    public static final ClientboundClearDialogPacket INSTANCE = new ClientboundClearDialogPacket();
    public static final StreamCodec<ByteBuf, ClientboundClearDialogPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private ClientboundClearDialogPacket() {
    }

    @Override
    public PacketType<ClientboundClearDialogPacket> type() {
        return CommonPacketTypes.CLIENTBOUND_CLEAR_DIALOG;
    }

    @Override
    public void handle(ClientCommonPacketListener listener) {
        listener.handleClearDialog(this);
    }
}

