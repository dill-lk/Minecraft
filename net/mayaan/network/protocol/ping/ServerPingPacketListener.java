/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.ping;

import net.mayaan.network.PacketListener;
import net.mayaan.network.protocol.ping.ServerboundPingRequestPacket;

public interface ServerPingPacketListener
extends PacketListener {
    public void handlePingRequest(ServerboundPingRequestPacket var1);
}

