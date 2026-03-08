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

public class ServerboundContainerClosePacket
implements Packet<ServerGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundContainerClosePacket> STREAM_CODEC = Packet.codec(ServerboundContainerClosePacket::write, ServerboundContainerClosePacket::new);
    private final int containerId;

    public ServerboundContainerClosePacket(int containerId) {
        this.containerId = containerId;
    }

    private ServerboundContainerClosePacket(FriendlyByteBuf input) {
        this.containerId = input.readContainerId();
    }

    private void write(FriendlyByteBuf output) {
        output.writeContainerId(this.containerId);
    }

    @Override
    public PacketType<ServerboundContainerClosePacket> type() {
        return GamePacketTypes.SERVERBOUND_CONTAINER_CLOSE;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleContainerClose(this);
    }

    public int getContainerId() {
        return this.containerId;
    }
}

