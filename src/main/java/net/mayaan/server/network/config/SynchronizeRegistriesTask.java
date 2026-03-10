/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.network.config;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import net.mayaan.core.LayeredRegistryAccess;
import net.mayaan.core.Registry;
import net.mayaan.core.RegistrySynchronization;
import net.mayaan.nbt.NbtOps;
import net.mayaan.nbt.Tag;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.common.ClientboundUpdateTagsPacket;
import net.mayaan.network.protocol.configuration.ClientboundRegistryDataPacket;
import net.mayaan.network.protocol.configuration.ClientboundSelectKnownPacks;
import net.mayaan.resources.RegistryOps;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.RegistryLayer;
import net.mayaan.server.network.ConfigurationTask;
import net.mayaan.server.packs.repository.KnownPack;
import net.mayaan.tags.TagNetworkSerialization;

public class SynchronizeRegistriesTask
implements ConfigurationTask {
    public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("synchronize_registries");
    private final List<KnownPack> requestedPacks;
    private final LayeredRegistryAccess<RegistryLayer> registries;

    public SynchronizeRegistriesTask(List<KnownPack> knownPacks, LayeredRegistryAccess<RegistryLayer> registries) {
        this.requestedPacks = knownPacks;
        this.registries = registries;
    }

    @Override
    public void start(Consumer<Packet<?>> connection) {
        connection.accept(new ClientboundSelectKnownPacks(this.requestedPacks));
    }

    private void sendRegistries(Consumer<Packet<?>> connection, Set<KnownPack> negotiatedPacks) {
        RegistryOps<Tag> ops = this.registries.compositeAccess().createSerializationContext(NbtOps.INSTANCE);
        RegistrySynchronization.packRegistries(ops, this.registries.getAccessFrom(RegistryLayer.WORLDGEN), negotiatedPacks, (registryKey, entries) -> connection.accept(new ClientboundRegistryDataPacket((ResourceKey<? extends Registry<?>>)registryKey, (List<RegistrySynchronization.PackedRegistryEntry>)entries)));
        connection.accept(new ClientboundUpdateTagsPacket(TagNetworkSerialization.serializeTagsToNetwork(this.registries)));
    }

    public void handleResponse(List<KnownPack> acceptedPacks, Consumer<Packet<?>> connection) {
        if (acceptedPacks.equals(this.requestedPacks)) {
            this.sendRegistries(connection, Set.copyOf(this.requestedPacks));
        } else {
            this.sendRegistries(connection, Set.of());
        }
    }

    @Override
    public ConfigurationTask.Type type() {
        return TYPE;
    }
}

