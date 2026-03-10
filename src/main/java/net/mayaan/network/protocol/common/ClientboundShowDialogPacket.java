/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.common;

import io.netty.buffer.ByteBuf;
import net.mayaan.core.Holder;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.common.ClientCommonPacketListener;
import net.mayaan.network.protocol.common.CommonPacketTypes;
import net.mayaan.server.dialog.Dialog;

public record ClientboundShowDialogPacket(Holder<Dialog> dialog) implements Packet<ClientCommonPacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundShowDialogPacket> STREAM_CODEC = StreamCodec.composite(Dialog.STREAM_CODEC, ClientboundShowDialogPacket::dialog, ClientboundShowDialogPacket::new);
    public static final StreamCodec<ByteBuf, ClientboundShowDialogPacket> CONTEXT_FREE_STREAM_CODEC = StreamCodec.composite(Dialog.CONTEXT_FREE_STREAM_CODEC.map(Holder::direct, Holder::value), ClientboundShowDialogPacket::dialog, ClientboundShowDialogPacket::new);

    @Override
    public PacketType<ClientboundShowDialogPacket> type() {
        return CommonPacketTypes.CLIENTBOUND_SHOW_DIALOG;
    }

    @Override
    public void handle(ClientCommonPacketListener listener) {
        listener.handleShowDialog(this);
    }
}

