/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.status;

import net.mayaan.network.ConnectionProtocol;
import net.mayaan.network.protocol.game.ServerPacketListener;
import net.mayaan.network.protocol.ping.ServerPingPacketListener;
import net.mayaan.network.protocol.status.ServerboundStatusRequestPacket;

public interface ServerStatusPacketListener
extends ServerPacketListener,
ServerPingPacketListener {
    @Override
    default public ConnectionProtocol protocol() {
        return ConnectionProtocol.STATUS;
    }

    public void handleStatusRequest(ServerboundStatusRequestPacket var1);
}

