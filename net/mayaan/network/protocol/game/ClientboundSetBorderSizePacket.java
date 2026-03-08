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

public class ClientboundSetBorderSizePacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundSetBorderSizePacket> STREAM_CODEC = Packet.codec(ClientboundSetBorderSizePacket::write, ClientboundSetBorderSizePacket::new);
    private final double size;

    public ClientboundSetBorderSizePacket(WorldBorder border) {
        this.size = border.getLerpTarget();
    }

    private ClientboundSetBorderSizePacket(FriendlyByteBuf input) {
        this.size = input.readDouble();
    }

    private void write(FriendlyByteBuf output) {
        output.writeDouble(this.size);
    }

    @Override
    public PacketType<ClientboundSetBorderSizePacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_BORDER_SIZE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetBorderSize(this);
    }

    public double getSize() {
        return this.size;
    }
}

