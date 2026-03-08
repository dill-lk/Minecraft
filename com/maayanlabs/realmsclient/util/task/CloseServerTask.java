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
import com.maayanlabs.realmsclient.dto.RealmsServer;
import com.maayanlabs.realmsclient.exception.RetryCallException;
import com.maayanlabs.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
import com.maayanlabs.realmsclient.util.task.LongRunningTask;
import net.mayaan.network.chat.Component;
import org.slf4j.Logger;

public class CloseServerTask
extends LongRunningTask {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Component TITLE = Component.translatable("mco.configure.world.closing");
    private final RealmsServer serverData;
    private final RealmsConfigureWorldScreen configureScreen;

    public CloseServerTask(RealmsServer realmsServer, RealmsConfigureWorldScreen configureWorldScreen) {
        this.serverData = realmsServer;
        this.configureScreen = configureWorldScreen;
    }

    @Override
    public void run() {
        RealmsClient client = RealmsClient.getOrCreate();
        for (int i = 0; i < 25; ++i) {
            if (this.aborted()) {
                return;
            }
            try {
                boolean closeResult = client.close(this.serverData.id);
                if (!closeResult) continue;
                this.configureScreen.stateChanged();
                this.serverData.state = RealmsServer.State.CLOSED;
                CloseServerTask.setScreen(this.configureScreen);
                break;
            }
            catch (RetryCallException e) {
                if (this.aborted()) {
                    return;
                }
                CloseServerTask.pause(e.delaySeconds);
                continue;
            }
            catch (Exception e) {
                if (this.aborted()) {
                    return;
                }
                LOGGER.error("Failed to close server", (Throwable)e);
                this.error(e);
            }
        }
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }
}

