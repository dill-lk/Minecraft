/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.multiplayer;

import net.mayaan.client.multiplayer.ClientPacketListener;
import net.mayaan.network.protocol.ping.ClientboundPongResponsePacket;
import net.mayaan.network.protocol.ping.ServerboundPingRequestPacket;
import net.mayaan.util.Util;
import net.mayaan.util.debugchart.LocalSampleLogger;

public class PingDebugMonitor {
    private final ClientPacketListener connection;
    private final LocalSampleLogger delayTimer;

    public PingDebugMonitor(ClientPacketListener connection, LocalSampleLogger delayTimer) {
        this.connection = connection;
        this.delayTimer = delayTimer;
    }

    public void tick() {
        this.connection.send(new ServerboundPingRequestPacket(Util.getMillis()));
    }

    public void onPongReceived(ClientboundPongResponsePacket packet) {
        this.delayTimer.logSample(Util.getMillis() - packet.time());
    }
}

