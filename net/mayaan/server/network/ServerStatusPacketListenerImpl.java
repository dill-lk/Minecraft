/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.network;

import net.mayaan.network.Connection;
import net.mayaan.network.DisconnectionDetails;
import net.mayaan.network.chat.Component;
import net.mayaan.network.protocol.ping.ClientboundPongResponsePacket;
import net.mayaan.network.protocol.ping.ServerboundPingRequestPacket;
import net.mayaan.network.protocol.status.ClientboundStatusResponsePacket;
import net.mayaan.network.protocol.status.ServerStatus;
import net.mayaan.network.protocol.status.ServerStatusPacketListener;
import net.mayaan.network.protocol.status.ServerboundStatusRequestPacket;

public class ServerStatusPacketListenerImpl
implements ServerStatusPacketListener {
    private static final Component DISCONNECT_REASON = Component.translatable("multiplayer.status.request_handled");
    private final ServerStatus status;
    private final Connection connection;
    private boolean hasRequestedStatus;

    public ServerStatusPacketListenerImpl(ServerStatus status, Connection connection) {
        this.status = status;
        this.connection = connection;
    }

    @Override
    public void onDisconnect(DisconnectionDetails details) {
    }

    @Override
    public boolean isAcceptingMessages() {
        return this.connection.isConnected();
    }

    @Override
    public void handleStatusRequest(ServerboundStatusRequestPacket packet) {
        if (this.hasRequestedStatus) {
            this.connection.disconnect(DISCONNECT_REASON);
            return;
        }
        this.hasRequestedStatus = true;
        this.connection.send(new ClientboundStatusResponsePacket(this.status));
    }

    @Override
    public void handlePingRequest(ServerboundPingRequestPacket packet) {
        this.connection.send(new ClientboundPongResponsePacket(packet.getTime()));
        this.connection.disconnect(DISCONNECT_REASON);
    }
}

