/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.multiplayer;

import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.StringWidget;
import net.mayaan.client.gui.layouts.FrameLayout;
import net.mayaan.client.gui.layouts.LinearLayout;
import net.mayaan.client.gui.screens.ConnectScreen;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.network.Connection;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;

public class ServerReconfigScreen
extends Screen {
    private static final int DISCONNECT_TIME = 600;
    private final Connection connection;
    private Button disconnectButton;
    private int delayTicker;
    private final LinearLayout layout = LinearLayout.vertical();

    public ServerReconfigScreen(Component title, Connection connection) {
        super(title);
        this.connection = connection;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        this.layout.defaultCellSetting().alignHorizontallyCenter().padding(10);
        this.layout.addChild(new StringWidget(this.title, this.font));
        this.disconnectButton = this.layout.addChild(Button.builder(CommonComponents.GUI_DISCONNECT, b -> this.connection.disconnect(ConnectScreen.ABORT_CONNECTION)).build());
        this.disconnectButton.active = false;
        this.layout.arrangeElements();
        ServerReconfigScreen serverReconfigScreen = this;
        this.layout.visitWidgets(x$0 -> serverReconfigScreen.addRenderableWidget(x$0));
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        FrameLayout.centerInRectangle(this.layout, this.getRectangle());
    }

    @Override
    public void tick() {
        super.tick();
        ++this.delayTicker;
        if (this.delayTicker == 600) {
            this.disconnectButton.active = true;
        }
        if (this.connection.isConnected()) {
            this.connection.tick();
        } else {
            this.connection.handleDisconnection();
        }
    }
}

