/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.mayaan.network.protocol.common;

import com.google.common.collect.Lists;
import java.util.List;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.common.ClientCommonPacketListener;
import net.mayaan.network.protocol.common.CommonPacketTypes;
import net.mayaan.network.protocol.common.custom.BrandPayload;
import net.mayaan.network.protocol.common.custom.CustomPacketPayload;
import net.mayaan.network.protocol.common.custom.DiscardedPayload;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Util;

public record ClientboundCustomPayloadPacket(CustomPacketPayload payload) implements Packet<ClientCommonPacketListener>
{
    private static final int MAX_PAYLOAD_SIZE = 0x100000;
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCustomPayloadPacket> GAMEPLAY_STREAM_CODEC = CustomPacketPayload.codec((Identifier id) -> DiscardedPayload.codec(id, 0x100000), Util.make(Lists.newArrayList((Object[])new CustomPacketPayload.TypeAndCodec[]{new CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, BrandPayload>(BrandPayload.TYPE, BrandPayload.STREAM_CODEC)}), types -> {})).map(ClientboundCustomPayloadPacket::new, ClientboundCustomPayloadPacket::payload);
    public static final StreamCodec<FriendlyByteBuf, ClientboundCustomPayloadPacket> CONFIG_STREAM_CODEC = CustomPacketPayload.codec((Identifier id) -> DiscardedPayload.codec(id, 0x100000), List.of(new CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, BrandPayload>(BrandPayload.TYPE, BrandPayload.STREAM_CODEC))).map(ClientboundCustomPayloadPacket::new, ClientboundCustomPayloadPacket::payload);

    @Override
    public PacketType<ClientboundCustomPayloadPacket> type() {
        return CommonPacketTypes.CLIENTBOUND_CUSTOM_PAYLOAD;
    }

    @Override
    public void handle(ClientCommonPacketListener listener) {
        listener.handleCustomPayload(this);
    }
}

