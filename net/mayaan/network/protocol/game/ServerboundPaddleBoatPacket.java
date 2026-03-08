/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;

public class ServerboundPaddleBoatPacket
implements Packet<ServerGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundPaddleBoatPacket> STREAM_CODEC = Packet.codec(ServerboundPaddleBoatPacket::write, ServerboundPaddleBoatPacket::new);
    private final boolean left;
    private final boolean right;

    public ServerboundPaddleBoatPacket(boolean left, boolean right) {
        this.left = left;
        this.right = right;
    }

    private ServerboundPaddleBoatPacket(FriendlyByteBuf input) {
        this.left = input.readBoolean();
        this.right = input.readBoolean();
    }

    private void write(FriendlyByteBuf output) {
        output.writeBoolean(this.left);
        output.writeBoolean(this.right);
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handlePaddleBoat(this);
    }

    @Override
    public PacketType<ServerboundPaddleBoatPacket> type() {
        return GamePacketTypes.SERVERBOUND_PADDLE_BOAT;
    }

    public boolean getLeft() {
        return this.left;
    }

    public boolean getRight() {
        return this.right;
    }
}

