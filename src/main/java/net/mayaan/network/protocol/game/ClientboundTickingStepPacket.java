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
import net.mayaan.world.TickRateManager;

public record ClientboundTickingStepPacket(int tickSteps) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundTickingStepPacket> STREAM_CODEC = Packet.codec(ClientboundTickingStepPacket::write, ClientboundTickingStepPacket::new);

    private ClientboundTickingStepPacket(FriendlyByteBuf input) {
        this(input.readVarInt());
    }

    public static ClientboundTickingStepPacket from(TickRateManager manager) {
        return new ClientboundTickingStepPacket(manager.frozenTicksToRun());
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.tickSteps);
    }

    @Override
    public PacketType<ClientboundTickingStepPacket> type() {
        return GamePacketTypes.CLIENTBOUND_TICKING_STEP;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleTickingStep(this);
    }
}

