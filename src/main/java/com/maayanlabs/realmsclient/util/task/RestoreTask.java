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
import com.maayanlabs.realmsclient.dto.Backup;
import com.maayanlabs.realmsclient.exception.RealmsServiceException;
import com.maayanlabs.realmsclient.exception.RetryCallException;
import com.maayanlabs.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.maayanlabs.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
import com.maayanlabs.realmsclient.util.task.LongRunningTask;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.network.chat.Component;
import org.slf4j.Logger;

public class RestoreTask
extends LongRunningTask {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Component TITLE = Component.translatable("mco.backup.restoring");
    private final Backup backup;
    private final long realmId;
    private final RealmsConfigureWorldScreen lastScreen;

    public RestoreTask(Backup backup, long realmId, RealmsConfigureWorldScreen lastScreen) {
        this.backup = backup;
        this.realmId = realmId;
        this.lastScreen = lastScreen;
    }

    @Override
    public void run() {
        RealmsClient client = RealmsClient.getOrCreate();
        for (int i = 0; i < 25; ++i) {
            try {
                if (this.aborted()) {
                    return;
                }
                client.restoreWorld(this.realmId, this.backup.backupId);
                RestoreTask.pause(1L);
                if (this.aborted()) {
                    return;
                }
                RestoreTask.setScreen(this.lastScreen);
                return;
            }
            catch (RetryCallException e) {
                if (this.aborted()) {
                    return;
                }
                RestoreTask.pause(e.delaySeconds);
                continue;
            }
            catch (RealmsServiceException e) {
                if (this.aborted()) {
                    return;
                }
                LOGGER.error("Couldn't restore backup", (Throwable)e);
                RestoreTask.setScreen(new RealmsGenericErrorScreen(e, (Screen)this.lastScreen));
                return;
            }
            catch (Exception e) {
                if (this.aborted()) {
                    return;
                }
                LOGGER.error("Couldn't restore backup", (Throwable)e);
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

