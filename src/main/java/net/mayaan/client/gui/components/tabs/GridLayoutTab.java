/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.components.tabs;

import java.util.function.Consumer;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.components.tabs.Tab;
import net.mayaan.client.gui.layouts.FrameLayout;
import net.mayaan.client.gui.layouts.GridLayout;
import net.mayaan.client.gui.navigation.ScreenRectangle;
import net.mayaan.network.chat.Component;

public class GridLayoutTab
implements Tab {
    private final Component title;
    protected final GridLayout layout = new GridLayout();

    public GridLayoutTab(Component title) {
        this.title = title;
    }

    @Override
    public Component getTabTitle() {
        return this.title;
    }

    @Override
    public Component getTabExtraNarration() {
        return Component.empty();
    }

    @Override
    public void visitChildren(Consumer<AbstractWidget> childrenConsumer) {
        this.layout.visitWidgets(childrenConsumer);
    }

    @Override
    public void doLayout(ScreenRectangle screenRectangle) {
        this.layout.arrangeElements();
        FrameLayout.alignInRectangle(this.layout, screenRectangle, 0.5f, 0.16666667f);
    }
}

