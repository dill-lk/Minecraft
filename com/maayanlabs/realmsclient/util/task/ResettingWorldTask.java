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
import com.maayanlabs.realmsclient.exception.RealmsServiceException;
import com.maayanlabs.realmsclient.exception.RetryCallException;
import com.maayanlabs.realmsclient.util.task.LongRunningTask;
import net.mayaan.network.chat.Component;
import org.slf4j.Logger;

public abstract class ResettingWorldTask
extends LongRunningTask {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final long serverId;
    private final Component title;
    private final Runnable callback;

    public ResettingWorldTask(long serverId, Component title, Runnable callback) {
        this.serverId = serverId;
        this.title = title;
        this.callback = callback;
    }

    protected abstract void sendResetRequest(RealmsClient var1, long var2) throws RealmsServiceException;

    @Override
    public void run() {
        RealmsClient client = RealmsClient.getOrCreate();
        for (int i = 0; i < 25; ++i) {
            try {
                if (this.aborted()) {
                    return;
                }
                this.sendResetRequest(client, this.serverId);
                if (this.aborted()) {
                    return;
                }
                this.callback.run();
                return;
            }
            catch (RetryCallException e) {
                if (this.aborted()) {
                    return;
                }
                ResettingWorldTask.pause(e.delaySeconds);
                continue;
            }
            catch (Exception e) {
                if (this.aborted()) {
                    return;
                }
                LOGGER.error("Couldn't reset world");
                this.error(e);
                return;
            }
        }
    }

    @Override
    public Component getTitle() {
        return this.title;
    }
}

