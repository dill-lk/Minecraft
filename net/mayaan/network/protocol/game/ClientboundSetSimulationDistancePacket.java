/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public record ClientboundSetSimulationDistancePacket(int simulationDistance) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundSetSimulationDistancePacket> STREAM_CODEC = Packet.codec(ClientboundSetSimulationDistancePacket::write, ClientboundSetSimulationDistancePacket::new);

    private ClientboundSetSimulationDistancePacket(FriendlyByteBuf input) {
        this(input.readVarInt());
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.simulationDistance);
    }

    @Override
    public PacketType<ClientboundSetSimulationDistancePacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_SIMULATION_DISTANCE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetSimulationDistance(this);
    }
}

