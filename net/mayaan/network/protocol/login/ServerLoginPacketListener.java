/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.login;

import net.mayaan.network.ConnectionProtocol;
import net.mayaan.network.protocol.cookie.ServerCookiePacketListener;
import net.mayaan.network.protocol.login.ServerboundCustomQueryAnswerPacket;
import net.mayaan.network.protocol.login.ServerboundHelloPacket;
import net.mayaan.network.protocol.login.ServerboundKeyPacket;
import net.mayaan.network.protocol.login.ServerboundLoginAcknowledgedPacket;

public interface ServerLoginPacketListener
extends ServerCookiePacketListener {
    @Override
    default public ConnectionProtocol protocol() {
        return ConnectionProtocol.LOGIN;
    }

    public void handleHello(ServerboundHelloPacket var1);

    public void handleKey(ServerboundKeyPacket var1);

    public void handleCustomQueryPacket(ServerboundCustomQueryAnswerPacket var1);

    public void handleLoginAcknowledgement(ServerboundLoginAcknowledgedPacket var1);
}

