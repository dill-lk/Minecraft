/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.login;

import net.mayaan.network.ConnectionProtocol;
import net.mayaan.network.protocol.cookie.ClientCookiePacketListener;
import net.mayaan.network.protocol.login.ClientboundCustomQueryPacket;
import net.mayaan.network.protocol.login.ClientboundHelloPacket;
import net.mayaan.network.protocol.login.ClientboundLoginCompressionPacket;
import net.mayaan.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.mayaan.network.protocol.login.ClientboundLoginFinishedPacket;

public interface ClientLoginPacketListener
extends ClientCookiePacketListener {
    @Override
    default public ConnectionProtocol protocol() {
        return ConnectionProtocol.LOGIN;
    }

    public void handleHello(ClientboundHelloPacket var1);

    public void handleLoginFinished(ClientboundLoginFinishedPacket var1);

    public void handleDisconnect(ClientboundLoginDisconnectPacket var1);

    public void handleCompression(ClientboundLoginCompressionPacket var1);

    public void handleCustomQuery(ClientboundCustomQueryPacket var1);
}

