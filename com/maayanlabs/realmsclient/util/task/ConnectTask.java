/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.realmsclient.util.task;

import com.maayanlabs.realmsclient.dto.RealmsJoinInformation;
import com.maayanlabs.realmsclient.dto.RealmsServer;
import com.maayanlabs.realmsclient.util.task.LongRunningTask;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.multiplayer.resolver.ServerAddress;
import net.mayaan.network.chat.Component;
import net.mayaan.realms.RealmsConnect;

public class ConnectTask
extends LongRunningTask {
    private static final Component TITLE = Component.translatable("mco.connect.connecting");
    private final RealmsConnect realmsConnect;
    private final RealmsServer server;
    private final RealmsJoinInformation address;

    public ConnectTask(Screen lastScreen, RealmsServer server, RealmsJoinInformation address) {
        this.server = server;
        this.address = address;
        this.realmsConnect = new RealmsConnect(lastScreen);
    }

    @Override
    public void run() {
        if (this.address.address() != null) {
            this.realmsConnect.connect(this.server, ServerAddress.parseString(this.address.address()));
        } else {
            this.abortTask();
        }
    }

    @Override
    public void abortTask() {
        super.abortTask();
        this.realmsConnect.abort();
        Mayaan.getInstance().getDownloadedPackSource().cleanupAfterDisconnect();
    }

    @Override
    public void tick() {
        this.realmsConnect.tick();
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }
}

