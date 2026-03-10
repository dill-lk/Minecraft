/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.configuration;

import java.util.HashSet;
import java.util.Set;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.configuration.ClientConfigurationPacketListener;
import net.mayaan.network.protocol.configuration.ConfigurationPacketTypes;
import net.mayaan.resources.Identifier;

public record ClientboundUpdateEnabledFeaturesPacket(Set<Identifier> features) implements Packet<ClientConfigurationPacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundUpdateEnabledFeaturesPacket> STREAM_CODEC = Packet.codec(ClientboundUpdateEnabledFeaturesPacket::write, ClientboundUpdateEnabledFeaturesPacket::new);

    private ClientboundUpdateEnabledFeaturesPacket(FriendlyByteBuf input) {
        this(input.readCollection(HashSet::new, FriendlyByteBuf::readIdentifier));
    }

    private void write(FriendlyByteBuf output) {
        output.writeCollection(this.features, FriendlyByteBuf::writeIdentifier);
    }

    @Override
    public PacketType<ClientboundUpdateEnabledFeaturesPacket> type() {
        return ConfigurationPacketTypes.CLIENTBOUND_UPDATE_ENABLED_FEATURES;
    }

    @Override
    public void handle(ClientConfigurationPacketListener listener) {
        listener.handleEnabledFeatures(this);
    }
}

