/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.components.tabs;

import java.util.function.Consumer;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.components.LoadingDotsWidget;
import net.mayaan.client.gui.components.tabs.Tab;
import net.mayaan.client.gui.layouts.FrameLayout;
import net.mayaan.client.gui.layouts.LinearLayout;
import net.mayaan.client.gui.navigation.ScreenRectangle;
import net.mayaan.network.chat.Component;

public class LoadingTab
implements Tab {
    private final Component title;
    private final Component loadingTitle;
    protected final LinearLayout layout = LinearLayout.vertical();

    public LoadingTab(Font font, Component title, Component loadingTitle) {
        this.title = title;
        this.loadingTitle = loadingTitle;
        LoadingDotsWidget loadingDotsWidget = new LoadingDotsWidget(font, loadingTitle);
        this.layout.defaultCellSetting().alignVerticallyMiddle().alignHorizontallyCenter();
        this.layout.addChild(loadingDotsWidget, layoutSettings -> layoutSettings.paddingBottom(30));
    }

    @Override
    public Component getTabTitle() {
        return this.title;
    }

    @Override
    public Component getTabExtraNarration() {
        return this.loadingTitle;
    }

    @Override
    public void visitChildren(Consumer<AbstractWidget> childrenConsumer) {
        this.layout.visitWidgets(childrenConsumer);
    }

    @Override
    public void doLayout(ScreenRectangle screenRectangle) {
        this.layout.arrangeElements();
        FrameLayout.alignInRectangle(this.layout, screenRectangle, 0.5f, 0.5f);
    }
}

