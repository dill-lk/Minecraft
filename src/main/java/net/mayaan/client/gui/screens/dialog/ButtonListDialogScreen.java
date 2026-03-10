/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.dialog;

import java.util.List;
import java.util.stream.Stream;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.layouts.HeaderAndFooterLayout;
import net.mayaan.client.gui.layouts.LinearLayout;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.dialog.DialogConnectionAccess;
import net.mayaan.client.gui.screens.dialog.DialogControlSet;
import net.mayaan.client.gui.screens.dialog.DialogScreen;
import net.mayaan.server.dialog.ActionButton;
import net.mayaan.server.dialog.ButtonListDialog;
import org.jspecify.annotations.Nullable;

public abstract class ButtonListDialogScreen<T extends ButtonListDialog>
extends DialogScreen<T> {
    public static final int FOOTER_MARGIN = 5;

    public ButtonListDialogScreen(@Nullable Screen previousScreen, T dialog, DialogConnectionAccess connectionAccess) {
        super(previousScreen, dialog, connectionAccess);
    }

    @Override
    protected void populateBodyElements(LinearLayout layout, DialogControlSet controlSet, T dialog, DialogConnectionAccess connectionAccess) {
        super.populateBodyElements(layout, controlSet, dialog, connectionAccess);
        List<Button> buttons = this.createListActions(dialog, connectionAccess).map(d -> controlSet.createActionButton((ActionButton)d).build()).toList();
        layout.addChild(ButtonListDialogScreen.packControlsIntoColumns(buttons, dialog.columns()));
    }

    protected abstract Stream<ActionButton> createListActions(T var1, DialogConnectionAccess var2);

    @Override
    protected void updateHeaderAndFooter(HeaderAndFooterLayout layout, DialogControlSet controlSet, T dialog, DialogConnectionAccess connectionAccess) {
        super.updateHeaderAndFooter(layout, controlSet, dialog, connectionAccess);
        dialog.exitAction().ifPresentOrElse(exitButton -> layout.addToFooter(controlSet.createActionButton((ActionButton)exitButton).build()), () -> layout.setFooterHeight(5));
    }
}

