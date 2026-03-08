/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.game;

import java.util.Objects;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.scores.DisplaySlot;
import net.mayaan.world.scores.Objective;
import org.jspecify.annotations.Nullable;

public class ClientboundSetDisplayObjectivePacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundSetDisplayObjectivePacket> STREAM_CODEC = Packet.codec(ClientboundSetDisplayObjectivePacket::write, ClientboundSetDisplayObjectivePacket::new);
    private final DisplaySlot slot;
    private final String objectiveName;

    public ClientboundSetDisplayObjectivePacket(DisplaySlot slot, @Nullable Objective objective) {
        this.slot = slot;
        this.objectiveName = objective == null ? "" : objective.getName();
    }

    private ClientboundSetDisplayObjectivePacket(FriendlyByteBuf input) {
        this.slot = input.readById(DisplaySlot.BY_ID);
        this.objectiveName = input.readUtf();
    }

    private void write(FriendlyByteBuf output) {
        output.writeById(DisplaySlot::id, this.slot);
        output.writeUtf(this.objectiveName);
    }

    @Override
    public PacketType<ClientboundSetDisplayObjectivePacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_DISPLAY_OBJECTIVE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetDisplayObjective(this);
    }

    public DisplaySlot getSlot() {
        return this.slot;
    }

    public @Nullable String getObjectiveName() {
        return Objects.equals(this.objectiveName, "") ? null : this.objectiveName;
    }
}

