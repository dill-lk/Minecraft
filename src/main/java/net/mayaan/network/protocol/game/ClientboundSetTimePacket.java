/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import java.util.HashMap;
import java.util.Map;
import net.mayaan.core.Holder;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.clock.ClockState;
import net.mayaan.world.clock.WorldClock;

public record ClientboundSetTimePacket(long gameTime, Map<Holder<WorldClock>, ClockState> clockUpdates) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetTimePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.LONG, ClientboundSetTimePacket::gameTime, ByteBufCodecs.map(HashMap::new, WorldClock.STREAM_CODEC, ClockState.STREAM_CODEC), ClientboundSetTimePacket::clockUpdates, ClientboundSetTimePacket::new);

    @Override
    public PacketType<ClientboundSetTimePacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_TIME;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetTime(this);
    }
}

