/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.blaze3d.platform;

import java.io.File;
import java.time.Duration;
import net.mayaan.CrashReport;
import net.mayaan.client.Mayaan;
import net.mayaan.server.dedicated.ServerWatchdog;

public class ClientShutdownWatchdog {
    private static final Duration CRASH_REPORT_PRELOAD_LOAD = Duration.ofSeconds(15L);

    public static void startShutdownWatchdog(Mayaan minecraft, File gameDirectory, long mainThreadId) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep((Duration)CRASH_REPORT_PRELOAD_LOAD);
            }
            catch (InterruptedException e) {
                return;
            }
            CrashReport report = ServerWatchdog.createWatchdogCrashReport("Client shutdown", mainThreadId);
            minecraft.fillReport(report);
            Mayaan.saveReport(gameDirectory, report);
        });
        thread.setDaemon(true);
        thread.setName("Client shutdown watchdog");
        thread.start();
    }
}

