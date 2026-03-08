/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.configuration;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.mayaan.core.Registry;
import net.mayaan.core.RegistrySynchronization;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.configuration.ClientConfigurationPacketListener;
import net.mayaan.network.protocol.configuration.ConfigurationPacketTypes;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;

public record ClientboundRegistryDataPacket(ResourceKey<? extends Registry<?>> registry, List<RegistrySynchronization.PackedRegistryEntry> entries) implements Packet<ClientConfigurationPacketListener>
{
    private static final StreamCodec<ByteBuf, ResourceKey<? extends Registry<?>>> REGISTRY_KEY_STREAM_CODEC = Identifier.STREAM_CODEC.map(ResourceKey::createRegistryKey, ResourceKey::identifier);
    public static final StreamCodec<FriendlyByteBuf, ClientboundRegistryDataPacket> STREAM_CODEC = StreamCodec.composite(REGISTRY_KEY_STREAM_CODEC, ClientboundRegistryDataPacket::registry, RegistrySynchronization.PackedRegistryEntry.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientboundRegistryDataPacket::entries, ClientboundRegistryDataPacket::new);

    @Override
    public PacketType<ClientboundRegistryDataPacket> type() {
        return ConfigurationPacketTypes.CLIENTBOUND_REGISTRY_DATA;
    }

    @Override
    public void handle(ClientConfigurationPacketListener listener) {
        listener.handleRegistryData(this);
    }
}

