/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  org.slf4j.Logger
 */
package com.maayanlabs.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.maayanlabs.realmsclient.client.RealmsClient;
import com.maayanlabs.realmsclient.dto.WorldDownload;
import com.maayanlabs.realmsclient.exception.RealmsServiceException;
import com.maayanlabs.realmsclient.exception.RetryCallException;
import com.maayanlabs.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
import com.maayanlabs.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.maayanlabs.realmsclient.util.task.LongRunningTask;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.network.chat.Component;
import org.slf4j.Logger;

public class DownloadTask
extends LongRunningTask {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Component TITLE = Component.translatable("mco.download.preparing");
    private final long realmId;
    private final int slot;
    private final Screen lastScreen;
    private final String downloadName;

    public DownloadTask(long realmId, int slot, String downloadName, Screen lastScreen) {
        this.realmId = realmId;
        this.slot = slot;
        this.lastScreen = lastScreen;
        this.downloadName = downloadName;
    }

    @Override
    public void run() {
        RealmsClient client = RealmsClient.getOrCreate();
        for (int i = 0; i < 25; ++i) {
            try {
                if (this.aborted()) {
                    return;
                }
                WorldDownload worldDownload = client.requestDownloadInfo(this.realmId, this.slot);
                DownloadTask.pause(1L);
                if (this.aborted()) {
                    return;
                }
                DownloadTask.setScreen(new RealmsDownloadLatestWorldScreen(this.lastScreen, worldDownload, this.downloadName, result -> {}));
                return;
            }
            catch (RetryCallException e) {
                if (this.aborted()) {
                    return;
                }
                DownloadTask.pause(e.delaySeconds);
                continue;
            }
            catch (RealmsServiceException e) {
                if (this.aborted()) {
                    return;
                }
                LOGGER.error("Couldn't download world data", (Throwable)e);
                DownloadTask.setScreen(new RealmsGenericErrorScreen(e, this.lastScreen));
                return;
            }
            catch (Exception e) {
                if (this.aborted()) {
                    return;
                }
                LOGGER.error("Couldn't download world data", (Throwable)e);
                this.error(e);
                return;
            }
        }
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }
}

