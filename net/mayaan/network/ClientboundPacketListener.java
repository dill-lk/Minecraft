/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network;

import net.mayaan.network.PacketListener;
import net.mayaan.network.protocol.PacketFlow;

public interface ClientboundPacketListener
extends PacketListener {
    @Override
    default public PacketFlow flow() {
        return PacketFlow.CLIENTBOUND;
    }
}

