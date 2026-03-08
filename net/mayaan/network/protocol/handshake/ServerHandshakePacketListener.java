/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.handshake;

import net.mayaan.network.ConnectionProtocol;
import net.mayaan.network.protocol.game.ServerPacketListener;
import net.mayaan.network.protocol.handshake.ClientIntentionPacket;

public interface ServerHandshakePacketListener
extends ServerPacketListener {
    @Override
    default public ConnectionProtocol protocol() {
        return ConnectionProtocol.HANDSHAKING;
    }

    public void handleIntention(ClientIntentionPacket var1);
}

