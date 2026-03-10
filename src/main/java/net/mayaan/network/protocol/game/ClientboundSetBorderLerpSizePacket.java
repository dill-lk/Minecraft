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

public class ClientboundSetBorderLerpSizePacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundSetBorderLerpSizePacket> STREAM_CODEC = Packet.codec(ClientboundSetBorderLerpSizePacket::write, ClientboundSetBorderLerpSizePacket::new);
    private final double oldSize;
    private final double newSize;
    private final long lerpTime;

    public ClientboundSetBorderLerpSizePacket(WorldBorder border) {
        this.oldSize = border.getSize();
        this.newSize = border.getLerpTarget();
        this.lerpTime = border.getLerpTime();
    }

    private ClientboundSetBorderLerpSizePacket(FriendlyByteBuf input) {
        this.oldSize = input.readDouble();
        this.newSize = input.readDouble();
        this.lerpTime = input.readVarLong();
    }

    private void write(FriendlyByteBuf output) {
        output.writeDouble(this.oldSize);
        output.writeDouble(this.newSize);
        output.writeVarLong(this.lerpTime);
    }

    @Override
    public PacketType<ClientboundSetBorderLerpSizePacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_BORDER_LERP_SIZE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetBorderLerpSize(this);
    }

    public double getOldSize() {
        return this.oldSize;
    }

    public double getNewSize() {
        return this.newSize;
    }

    public long getLerpTime() {
        return this.lerpTime;
    }
}

