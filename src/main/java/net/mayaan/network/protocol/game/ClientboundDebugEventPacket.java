/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.util.debug.DebugSubscription;

public record ClientboundDebugEventPacket(DebugSubscription.Event<?> event) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDebugEventPacket> STREAM_CODEC = StreamCodec.composite(DebugSubscription.Event.STREAM_CODEC, ClientboundDebugEventPacket::event, ClientboundDebugEventPacket::new);

    @Override
    public PacketType<ClientboundDebugEventPacket> type() {
        return GamePacketTypes.CLIENTBOUND_DEBUG_EVENT;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleDebugEvent(this);
    }
}

