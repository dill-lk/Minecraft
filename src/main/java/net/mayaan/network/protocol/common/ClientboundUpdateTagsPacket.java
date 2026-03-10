/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.common;

import java.util.Map;
import net.mayaan.core.Registry;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.common.ClientCommonPacketListener;
import net.mayaan.network.protocol.common.CommonPacketTypes;
import net.mayaan.resources.ResourceKey;
import net.mayaan.tags.TagNetworkSerialization;

public class ClientboundUpdateTagsPacket
implements Packet<ClientCommonPacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundUpdateTagsPacket> STREAM_CODEC = Packet.codec(ClientboundUpdateTagsPacket::write, ClientboundUpdateTagsPacket::new);
    private final Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> tags;

    public ClientboundUpdateTagsPacket(Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> tags) {
        this.tags = tags;
    }

    private ClientboundUpdateTagsPacket(FriendlyByteBuf input) {
        this.tags = input.readMap(FriendlyByteBuf::readRegistryKey, TagNetworkSerialization.NetworkPayload::read);
    }

    private void write(FriendlyByteBuf output) {
        output.writeMap(this.tags, FriendlyByteBuf::writeResourceKey, (buffer, value) -> value.write((FriendlyByteBuf)((Object)buffer)));
    }

    @Override
    public PacketType<ClientboundUpdateTagsPacket> type() {
        return CommonPacketTypes.CLIENTBOUND_UPDATE_TAGS;
    }

    @Override
    public void handle(ClientCommonPacketListener listener) {
        listener.handleUpdateTags(this);
    }

    public Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> getTags() {
        return this.tags;
    }
}

