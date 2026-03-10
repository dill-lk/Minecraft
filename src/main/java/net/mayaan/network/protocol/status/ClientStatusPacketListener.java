/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.status;

import net.mayaan.network.ClientboundPacketListener;
import net.mayaan.network.ConnectionProtocol;
import net.mayaan.network.protocol.ping.ClientPongPacketListener;
import net.mayaan.network.protocol.status.ClientboundStatusResponsePacket;

public interface ClientStatusPacketListener
extends ClientboundPacketListener,
ClientPongPacketListener {
    @Override
    default public ConnectionProtocol protocol() {
        return ConnectionProtocol.STATUS;
    }

    public void handleStatusResponse(ClientboundStatusResponsePacket var1);
}

