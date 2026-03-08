/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.dialog.input;

import net.mayaan.client.gui.layouts.LayoutElement;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.server.dialog.action.Action;
import net.mayaan.server.dialog.input.InputControl;

@FunctionalInterface
public interface InputControlHandler<T extends InputControl> {
    public void addControl(T var1, Screen var2, Output var3);

    @FunctionalInterface
    public static interface Output {
        public void accept(LayoutElement var1, Action.ValueGetter var2);
    }
}

