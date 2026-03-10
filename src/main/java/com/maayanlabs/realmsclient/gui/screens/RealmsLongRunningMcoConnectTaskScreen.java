/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.realmsclient.gui.screens;

import com.maayanlabs.realmsclient.dto.RealmsJoinInformation;
import com.maayanlabs.realmsclient.dto.ServiceQuality;
import com.maayanlabs.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.maayanlabs.realmsclient.util.task.LongRunningTask;
import net.mayaan.client.gui.components.ImageWidget;
import net.mayaan.client.gui.components.StringWidget;
import net.mayaan.client.gui.layouts.FrameLayout;
import net.mayaan.client.gui.layouts.LayoutSettings;
import net.mayaan.client.gui.layouts.LinearLayout;
import net.mayaan.client.gui.navigation.ScreenRectangle;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;

public class RealmsLongRunningMcoConnectTaskScreen
extends RealmsLongRunningMcoTaskScreen {
    private final LongRunningTask task;
    private final RealmsJoinInformation serverAddress;
    private final LinearLayout footer = LinearLayout.vertical();

    public RealmsLongRunningMcoConnectTaskScreen(Screen lastScreen, RealmsJoinInformation serverAddress, LongRunningTask task) {
        super(lastScreen, task);
        this.task = task;
        this.serverAddress = serverAddress;
    }

    @Override
    public void init() {
        super.init();
        if (this.serverAddress.regionData() == null || this.serverAddress.regionData().region() == null) {
            return;
        }
        LinearLayout regionInfo = LinearLayout.horizontal().spacing(10);
        StringWidget region = new StringWidget(Component.translatable("mco.connect.region", Component.translatable(this.serverAddress.regionData().region().translationKey)), this.font);
        regionInfo.addChild(region);
        Identifier icon = this.serverAddress.regionData().serviceQuality() != null ? this.serverAddress.regionData().serviceQuality().getIcon() : ServiceQuality.UNKNOWN.getIcon();
        regionInfo.addChild(ImageWidget.sprite(10, 8, icon), LayoutSettings::alignVerticallyTop);
        this.footer.addChild(regionInfo, layoutSettings -> layoutSettings.paddingTop(40));
        RealmsLongRunningMcoConnectTaskScreen realmsLongRunningMcoConnectTaskScreen = this;
        this.footer.visitWidgets(x$0 -> realmsLongRunningMcoConnectTaskScreen.addRenderableWidget(x$0));
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        super.repositionElements();
        int contentBottom = this.layout.getY() + this.layout.getHeight();
        ScreenRectangle footerRectangle = new ScreenRectangle(0, contentBottom, this.width, this.height - contentBottom);
        this.footer.arrangeElements();
        FrameLayout.alignInRectangle(this.footer, footerRectangle, 0.5f, 0.0f);
    }

    @Override
    public void tick() {
        super.tick();
        this.task.tick();
    }

    @Override
    protected void cancel() {
        this.task.abortTask();
        super.cancel();
    }
}

