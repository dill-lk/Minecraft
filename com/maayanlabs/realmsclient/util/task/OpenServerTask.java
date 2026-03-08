/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  org.slf4j.Logger
 */
package com.maayanlabs.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.maayanlabs.realmsclient.RealmsMainScreen;
import com.maayanlabs.realmsclient.client.RealmsClient;
import com.maayanlabs.realmsclient.dto.RealmsServer;
import com.maayanlabs.realmsclient.exception.RetryCallException;
import com.maayanlabs.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
import com.maayanlabs.realmsclient.util.task.LongRunningTask;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.network.chat.Component;
import org.slf4j.Logger;

public class OpenServerTask
extends LongRunningTask {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Component TITLE = Component.translatable("mco.configure.world.opening");
    private final RealmsServer serverData;
    private final Screen returnScreen;
    private final boolean join;
    private final Mayaan minecraft;

    public OpenServerTask(RealmsServer realmsServer, Screen returnScreen, boolean join, Mayaan minecraft) {
        this.serverData = realmsServer;
        this.returnScreen = returnScreen;
        this.join = join;
        this.minecraft = minecraft;
    }

    @Override
    public void run() {
        RealmsClient client = RealmsClient.getOrCreate();
        for (int i = 0; i < 25; ++i) {
            if (this.aborted()) {
                return;
            }
            try {
                boolean openResult = client.open(this.serverData.id);
                if (!openResult) continue;
                this.minecraft.execute(() -> {
                    Screen patt0$temp = this.returnScreen;
                    if (patt0$temp instanceof RealmsConfigureWorldScreen) {
                        RealmsConfigureWorldScreen screen = (RealmsConfigureWorldScreen)patt0$temp;
                        screen.stateChanged();
                    }
                    this.serverData.state = RealmsServer.State.OPEN;
                    if (this.join) {
                        RealmsMainScreen.play(this.serverData, this.returnScreen);
                    } else {
                        this.minecraft.setScreen(this.returnScreen);
                    }
                });
                break;
            }
            catch (RetryCallException e) {
                if (this.aborted()) {
                    return;
                }
                OpenServerTask.pause(e.delaySeconds);
                continue;
            }
            catch (Exception e) {
                if (this.aborted()) {
                    return;
                }
                LOGGER.error("Failed to open server", (Throwable)e);
                this.error(e);
            }
        }
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }
}

