/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import java.util.Locale;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.client.multiplayer.ClientPacketListener;
import net.mayaan.client.server.IntegratedServer;
import net.mayaan.network.Connection;
import net.mayaan.server.ServerTickRateManager;
import net.mayaan.world.TickRateManager;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugEntryTps
implements DebugScreenEntry {
    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        String tps;
        Mayaan minecraft = Mayaan.getInstance();
        IntegratedServer server = minecraft.getSingleplayerServer();
        ClientPacketListener connectionListener = minecraft.getConnection();
        if (connectionListener == null || serverOrClientLevel == null) {
            return;
        }
        Connection connection = connectionListener.getConnection();
        float averageSentPackets = connection.getAverageSentPackets();
        float averageReceivedPackets = connection.getAverageReceivedPackets();
        TickRateManager tickRateManager = serverOrClientLevel.tickRateManager();
        String runStatus = tickRateManager.isSteppingForward() ? " (frozen - stepping)" : (tickRateManager.isFrozen() ? " (frozen)" : "");
        if (server != null) {
            ServerTickRateManager serverTickRateManager = server.tickRateManager();
            boolean isSpriting = serverTickRateManager.isSprinting();
            if (isSpriting) {
                runStatus = " (sprinting)";
            }
            String tpsTarget = isSpriting ? "-" : String.format(Locale.ROOT, "%.1f", Float.valueOf(tickRateManager.millisecondsPerTick()));
            tps = String.format(Locale.ROOT, "Integrated server @ %.1f/%s ms%s, %.0f tx, %.0f rx", Float.valueOf(server.getCurrentSmoothedTickTime()), tpsTarget, runStatus, Float.valueOf(averageSentPackets), Float.valueOf(averageReceivedPackets));
        } else {
            tps = String.format(Locale.ROOT, "\"%s\" server%s, %.0f tx, %.0f rx", connectionListener.serverBrand(), runStatus, Float.valueOf(averageSentPackets), Float.valueOf(averageReceivedPackets));
        }
        displayer.addLine(tps);
    }

    @Override
    public boolean isAllowed(boolean reducedDebugInfo) {
        return true;
    }
}

