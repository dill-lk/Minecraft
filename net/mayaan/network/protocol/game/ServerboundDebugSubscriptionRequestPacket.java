/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
 */
package net.mayaan.network.protocol.game;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.util.debug.DebugSubscription;

public record ServerboundDebugSubscriptionRequestPacket(Set<DebugSubscription<?>> subscriptions) implements Packet<ServerGamePacketListener>
{
    private static final StreamCodec<RegistryFriendlyByteBuf, Set<DebugSubscription<?>>> SET_STREAM_CODEC = ByteBufCodecs.registry(Registries.DEBUG_SUBSCRIPTION).apply(ByteBufCodecs.collection(ReferenceOpenHashSet::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundDebugSubscriptionRequestPacket> STREAM_CODEC = SET_STREAM_CODEC.map(ServerboundDebugSubscriptionRequestPacket::new, ServerboundDebugSubscriptionRequestPacket::subscriptions);

    @Override
    public PacketType<ServerboundDebugSubscriptionRequestPacket> type() {
        return GamePacketTypes.SERVERBOUND_DEBUG_SUBSCRIPTION_REQUEST;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleDebugSubscriptionRequest(this);
    }
}

