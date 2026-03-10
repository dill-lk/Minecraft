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

public class ClientboundSetBorderWarningDistancePacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundSetBorderWarningDistancePacket> STREAM_CODEC = Packet.codec(ClientboundSetBorderWarningDistancePacket::write, ClientboundSetBorderWarningDistancePacket::new);
    private final int warningBlocks;

    public ClientboundSetBorderWarningDistancePacket(WorldBorder border) {
        this.warningBlocks = border.getWarningBlocks();
    }

    private ClientboundSetBorderWarningDistancePacket(FriendlyByteBuf input) {
        this.warningBlocks = input.readVarInt();
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.warningBlocks);
    }

    @Override
    public PacketType<ClientboundSetBorderWarningDistancePacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_BORDER_WARNING_DISTANCE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetBorderWarningDistance(this);
    }

    public int getWarningBlocks() {
        return this.warningBlocks;
    }
}

