/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.util.debug.DebugSubscription;

public record ClientboundDebugEntityValuePacket(int entityId, DebugSubscription.Update<?> update) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDebugEntityValuePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundDebugEntityValuePacket::entityId, DebugSubscription.Update.STREAM_CODEC, ClientboundDebugEntityValuePacket::update, ClientboundDebugEntityValuePacket::new);

    @Override
    public PacketType<ClientboundDebugEntityValuePacket> type() {
        return GamePacketTypes.CLIENTBOUND_DEBUG_ENTITY_VALUE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleDebugEntityValue(this);
    }
}

