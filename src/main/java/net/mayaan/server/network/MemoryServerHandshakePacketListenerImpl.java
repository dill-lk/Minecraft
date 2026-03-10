/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.network;

import net.mayaan.network.Connection;
import net.mayaan.network.DisconnectionDetails;
import net.mayaan.network.protocol.handshake.ClientIntent;
import net.mayaan.network.protocol.handshake.ClientIntentionPacket;
import net.mayaan.network.protocol.handshake.ServerHandshakePacketListener;
import net.mayaan.network.protocol.login.LoginProtocols;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.network.ServerLoginPacketListenerImpl;

public class MemoryServerHandshakePacketListenerImpl
implements ServerHandshakePacketListener {
    private final MayaanServer server;
    private final Connection connection;

    public MemoryServerHandshakePacketListenerImpl(MayaanServer server, Connection connection) {
        this.server = server;
        this.connection = connection;
    }

    @Override
    public void handleIntention(ClientIntentionPacket packet) {
        if (packet.intention() != ClientIntent.LOGIN) {
            throw new UnsupportedOperationException("Invalid intention " + String.valueOf((Object)packet.intention()));
        }
        this.connection.setupInboundProtocol(LoginProtocols.SERVERBOUND, new ServerLoginPacketListenerImpl(this.server, this.connection, false));
        this.connection.setupOutboundProtocol(LoginProtocols.CLIENTBOUND);
    }

    @Override
    public void onDisconnect(DisconnectionDetails details) {
    }

    @Override
    public boolean isAcceptingMessages() {
        return this.connection.isConnected();
    }
}

