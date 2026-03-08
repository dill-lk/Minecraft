/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import java.util.Optional;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.chat.numbers.NumberFormat;
import net.mayaan.network.chat.numbers.NumberFormatTypes;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.scores.Objective;
import net.mayaan.world.scores.criteria.ObjectiveCriteria;

public class ClientboundSetObjectivePacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetObjectivePacket> STREAM_CODEC = Packet.codec(ClientboundSetObjectivePacket::write, ClientboundSetObjectivePacket::new);
    public static final int METHOD_ADD = 0;
    public static final int METHOD_REMOVE = 1;
    public static final int METHOD_CHANGE = 2;
    private final String objectiveName;
    private final Component displayName;
    private final ObjectiveCriteria.RenderType renderType;
    private final Optional<NumberFormat> numberFormat;
    private final int method;

    public ClientboundSetObjectivePacket(Objective objective, int method) {
        this.objectiveName = objective.getName();
        this.displayName = objective.getDisplayName();
        this.renderType = objective.getRenderType();
        this.numberFormat = Optional.ofNullable(objective.numberFormat());
        this.method = method;
    }

    private ClientboundSetObjectivePacket(RegistryFriendlyByteBuf input) {
        this.objectiveName = input.readUtf();
        this.method = input.readByte();
        if (this.method == 0 || this.method == 2) {
            this.displayName = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
            this.renderType = input.readEnum(ObjectiveCriteria.RenderType.class);
            this.numberFormat = (Optional)NumberFormatTypes.OPTIONAL_STREAM_CODEC.decode(input);
        } else {
            this.displayName = CommonComponents.EMPTY;
            this.renderType = ObjectiveCriteria.RenderType.INTEGER;
            this.numberFormat = Optional.empty();
        }
    }

    private void write(RegistryFriendlyByteBuf output) {
        output.writeUtf(this.objectiveName);
        output.writeByte(this.method);
        if (this.method == 0 || this.method == 2) {
            ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.displayName);
            output.writeEnum(this.renderType);
            NumberFormatTypes.OPTIONAL_STREAM_CODEC.encode(output, this.numberFormat);
        }
    }

    @Override
    public PacketType<ClientboundSetObjectivePacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_OBJECTIVE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleAddObjective(this);
    }

    public String getObjectiveName() {
        return this.objectiveName;
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    public int getMethod() {
        return this.method;
    }

    public ObjectiveCriteria.RenderType getRenderType() {
        return this.renderType;
    }

    public Optional<NumberFormat> getNumberFormat() {
        return this.numberFormat;
    }
}

