/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.network;

import net.mayaan.SharedConstants;
import net.mayaan.network.Connection;
import net.mayaan.network.DisconnectionDetails;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.network.protocol.handshake.ClientIntentionPacket;
import net.mayaan.network.protocol.handshake.ServerHandshakePacketListener;
import net.mayaan.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.mayaan.network.protocol.login.LoginProtocols;
import net.mayaan.network.protocol.status.ServerStatus;
import net.mayaan.network.protocol.status.StatusProtocols;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.network.ServerLoginPacketListenerImpl;
import net.mayaan.server.network.ServerStatusPacketListenerImpl;

public class ServerHandshakePacketListenerImpl
implements ServerHandshakePacketListener {
    private static final Component IGNORE_STATUS_REASON = Component.translatable("disconnect.ignoring_status_request");
    private final MayaanServer server;
    private final Connection connection;

    public ServerHandshakePacketListenerImpl(MayaanServer server, Connection connection) {
        this.server = server;
        this.connection = connection;
    }

    @Override
    public void handleIntention(ClientIntentionPacket packet) {
        switch (packet.intention()) {
            case LOGIN: {
                this.beginLogin(packet, false);
                break;
            }
            case STATUS: {
                ServerStatus status = this.server.getStatus();
                this.connection.setupOutboundProtocol(StatusProtocols.CLIENTBOUND);
                if (this.server.repliesToStatus() && status != null) {
                    this.connection.setupInboundProtocol(StatusProtocols.SERVERBOUND, new ServerStatusPacketListenerImpl(status, this.connection));
                    break;
                }
                this.connection.disconnect(IGNORE_STATUS_REASON);
                break;
            }
            case TRANSFER: {
                if (!this.server.acceptsTransfers()) {
                    this.connection.setupOutboundProtocol(LoginProtocols.CLIENTBOUND);
                    MutableComponent reason = Component.translatable("multiplayer.disconnect.transfers_disabled");
                    this.connection.send(new ClientboundLoginDisconnectPacket(reason));
                    this.connection.disconnect(reason);
                    break;
                }
                this.beginLogin(packet, true);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Invalid intention " + String.valueOf((Object)packet.intention()));
            }
        }
    }

    private void beginLogin(ClientIntentionPacket packet, boolean transfer) {
        this.connection.setupOutboundProtocol(LoginProtocols.CLIENTBOUND);
        if (packet.protocolVersion() != SharedConstants.getCurrentVersion().protocolVersion()) {
            MutableComponent reason = packet.protocolVersion() < 754 ? Component.translatable("multiplayer.disconnect.outdated_client", SharedConstants.getCurrentVersion().name()) : Component.translatable("multiplayer.disconnect.incompatible", SharedConstants.getCurrentVersion().name());
            this.connection.send(new ClientboundLoginDisconnectPacket(reason));
            this.connection.disconnect(reason);
        } else {
            this.connection.setupInboundProtocol(LoginProtocols.SERVERBOUND, new ServerLoginPacketListenerImpl(this.server, this.connection, transfer));
        }
    }

    @Override
    public void onDisconnect(DisconnectionDetails details) {
    }

    @Override
    public boolean isAcceptingMessages() {
        return this.connection.isConnected();
    }
}

