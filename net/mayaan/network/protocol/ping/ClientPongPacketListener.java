/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.ping;

import net.mayaan.network.PacketListener;
import net.mayaan.network.protocol.ping.ClientboundPongResponsePacket;

public interface ClientPongPacketListener
extends PacketListener {
    public void handlePongResponse(ClientboundPongResponsePacket var1);
}

