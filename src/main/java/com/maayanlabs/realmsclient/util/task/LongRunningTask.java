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
import com.maayanlabs.realmsclient.exception.RealmsServiceException;
import com.maayanlabs.realmsclient.gui.screens.RealmsGenericErrorScreen;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.TitleScreen;
import net.mayaan.network.chat.Component;
import org.slf4j.Logger;

public abstract class LongRunningTask
implements Runnable {
    protected static final int NUMBER_OF_RETRIES = 25;
    private static final Logger LOGGER = LogUtils.getLogger();
    private boolean aborted = false;

    protected static void pause(long seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("", (Throwable)e);
        }
    }

    public static void setScreen(Screen screen) {
        Mayaan minecraft = Mayaan.getInstance();
        minecraft.execute(() -> minecraft.setScreen(screen));
    }

    protected void error(Component errorMessage) {
        this.abortTask();
        Mayaan minecraft = Mayaan.getInstance();
        minecraft.execute(() -> minecraft.setScreen(new RealmsGenericErrorScreen(errorMessage, (Screen)new RealmsMainScreen(new TitleScreen()))));
    }

    protected void error(Exception ex) {
        if (ex instanceof RealmsServiceException) {
            RealmsServiceException rsx = (RealmsServiceException)ex;
            this.error(rsx.realmsError.errorMessage());
        } else {
            this.error(Component.literal(ex.getMessage()));
        }
    }

    protected void error(RealmsServiceException ex) {
        this.error(ex.realmsError.errorMessage());
    }

    public abstract Component getTitle();

    public boolean aborted() {
        return this.aborted;
    }

    public void tick() {
    }

    public void init() {
    }

    public void abortTask() {
        this.aborted = true;
    }
}

