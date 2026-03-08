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

public class ClientboundContainerClosePacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundContainerClosePacket> STREAM_CODEC = Packet.codec(ClientboundContainerClosePacket::write, ClientboundContainerClosePacket::new);
    private final int containerId;

    public ClientboundContainerClosePacket(int containerId) {
        this.containerId = containerId;
    }

    private ClientboundContainerClosePacket(FriendlyByteBuf input) {
        this.containerId = input.readContainerId();
    }

    private void write(FriendlyByteBuf output) {
        output.writeContainerId(this.containerId);
    }

    @Override
    public PacketType<ClientboundContainerClosePacket> type() {
        return GamePacketTypes.CLIENTBOUND_CONTAINER_CLOSE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleContainerClose(this);
    }

    public int getContainerId() {
        return this.containerId;
    }
}

