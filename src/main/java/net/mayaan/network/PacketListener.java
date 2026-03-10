/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network;

import net.mayaan.CrashReport;
import net.mayaan.CrashReportCategory;
import net.mayaan.ReportedException;
import net.mayaan.network.ConnectionProtocol;
import net.mayaan.network.DisconnectionDetails;
import net.mayaan.network.chat.Component;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketFlow;
import net.mayaan.network.protocol.PacketUtils;

public interface PacketListener {
    public PacketFlow flow();

    public ConnectionProtocol protocol();

    public void onDisconnect(DisconnectionDetails var1);

    default public void onPacketError(Packet packet, Exception cause) throws ReportedException {
        throw PacketUtils.makeReportedException(cause, packet, this);
    }

    default public DisconnectionDetails createDisconnectionInfo(Component reason, Throwable cause) {
        return new DisconnectionDetails(reason);
    }

    public boolean isAcceptingMessages();

    default public boolean shouldHandleMessage(Packet<?> packet) {
        return this.isAcceptingMessages();
    }

    default public void fillCrashReport(CrashReport crashReport) {
        CrashReportCategory connection = crashReport.addCategory("Connection");
        connection.setDetail("Protocol", () -> this.protocol().id());
        connection.setDetail("Flow", () -> this.flow().toString());
        this.fillListenerSpecificCrashDetails(crashReport, connection);
    }

    default public void fillListenerSpecificCrashDetails(CrashReport report, CrashReportCategory connectionDetails) {
    }
}

