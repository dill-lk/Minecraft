/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.configuration;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.configuration.ClientConfigurationPacketListener;
import net.mayaan.network.protocol.configuration.ConfigurationPacketTypes;
import net.mayaan.server.packs.repository.KnownPack;

public record ClientboundSelectKnownPacks(List<KnownPack> knownPacks) implements Packet<ClientConfigurationPacketListener>
{
    public static final StreamCodec<ByteBuf, ClientboundSelectKnownPacks> STREAM_CODEC = StreamCodec.composite(KnownPack.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientboundSelectKnownPacks::knownPacks, ClientboundSelectKnownPacks::new);

    @Override
    public PacketType<ClientboundSelectKnownPacks> type() {
        return ConfigurationPacketTypes.CLIENTBOUND_SELECT_KNOWN_PACKS;
    }

    @Override
    public void handle(ClientConfigurationPacketListener listener) {
        listener.handleSelectKnownPacks(this);
    }
}

