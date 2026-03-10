/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.components.tabs;

import java.util.function.Consumer;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.navigation.ScreenRectangle;
import net.mayaan.network.chat.Component;

public interface Tab {
    public Component getTabTitle();

    public Component getTabExtraNarration();

    public void visitChildren(Consumer<AbstractWidget> var1);

    public void doLayout(ScreenRectangle var1);
}

