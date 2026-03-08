/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.mayaan.network.protocol.common;

import com.google.common.collect.Lists;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.common.CommonPacketTypes;
import net.mayaan.network.protocol.common.ServerCommonPacketListener;
import net.mayaan.network.protocol.common.custom.BrandPayload;
import net.mayaan.network.protocol.common.custom.CustomPacketPayload;
import net.mayaan.network.protocol.common.custom.DiscardedPayload;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Util;

public record ServerboundCustomPayloadPacket(CustomPacketPayload payload) implements Packet<ServerCommonPacketListener>
{
    private static final int MAX_PAYLOAD_SIZE = Short.MAX_VALUE;
    public static final StreamCodec<FriendlyByteBuf, ServerboundCustomPayloadPacket> STREAM_CODEC = CustomPacketPayload.codec((Identifier id) -> DiscardedPayload.codec(id, Short.MAX_VALUE), Util.make(Lists.newArrayList((Object[])new CustomPacketPayload.TypeAndCodec[]{new CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, BrandPayload>(BrandPayload.TYPE, BrandPayload.STREAM_CODEC)}), types -> {})).map(ServerboundCustomPayloadPacket::new, ServerboundCustomPayloadPacket::payload);

    @Override
    public PacketType<ServerboundCustomPayloadPacket> type() {
        return CommonPacketTypes.SERVERBOUND_CUSTOM_PAYLOAD;
    }

    @Override
    public void handle(ServerCommonPacketListener listener) {
        listener.handleCustomPayload(this);
    }
}

