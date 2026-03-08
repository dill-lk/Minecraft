/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.dialog.body;

import net.mayaan.client.gui.layouts.LayoutElement;
import net.mayaan.client.gui.screens.dialog.DialogScreen;
import net.mayaan.server.dialog.body.DialogBody;

public interface DialogBodyHandler<T extends DialogBody> {
    public LayoutElement createControls(DialogScreen<?> var1, T var2);
}

