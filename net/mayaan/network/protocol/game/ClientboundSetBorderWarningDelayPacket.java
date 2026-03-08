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
import net.mayaan.world.level.border.WorldBorder;

public class ClientboundSetBorderWarningDelayPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundSetBorderWarningDelayPacket> STREAM_CODEC = Packet.codec(ClientboundSetBorderWarningDelayPacket::write, ClientboundSetBorderWarningDelayPacket::new);
    private final int warningDelay;

    public ClientboundSetBorderWarningDelayPacket(WorldBorder border) {
        this.warningDelay = border.getWarningTime();
    }

    private ClientboundSetBorderWarningDelayPacket(FriendlyByteBuf input) {
        this.warningDelay = input.readVarInt();
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.warningDelay);
    }

    @Override
    public PacketType<ClientboundSetBorderWarningDelayPacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_BORDER_WARNING_DELAY;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetBorderWarningDelay(this);
    }

    public int getWarningDelay() {
        return this.warningDelay;
    }
}

