/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.dialog;

import net.mayaan.client.gui.layouts.HeaderAndFooterLayout;
import net.mayaan.client.gui.layouts.LinearLayout;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.dialog.DialogConnectionAccess;
import net.mayaan.client.gui.screens.dialog.DialogControlSet;
import net.mayaan.client.gui.screens.dialog.DialogScreen;
import net.mayaan.server.dialog.ActionButton;
import net.mayaan.server.dialog.SimpleDialog;
import org.jspecify.annotations.Nullable;

public class SimpleDialogScreen<T extends SimpleDialog>
extends DialogScreen<T> {
    public SimpleDialogScreen(@Nullable Screen previousScreen, T dialog, DialogConnectionAccess connectionAccess) {
        super(previousScreen, dialog, connectionAccess);
    }

    @Override
    protected void updateHeaderAndFooter(HeaderAndFooterLayout layout, DialogControlSet controlSet, T dialog, DialogConnectionAccess connectionAccess) {
        super.updateHeaderAndFooter(layout, controlSet, dialog, connectionAccess);
        LinearLayout buttonLayout = LinearLayout.horizontal().spacing(8);
        for (ActionButton action : dialog.mainActions()) {
            buttonLayout.addChild(controlSet.createActionButton(action).build());
        }
        layout.addToFooter(buttonLayout);
    }
}

