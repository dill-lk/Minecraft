/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.dialog;

import java.util.Optional;
import java.util.stream.Stream;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.dialog.ButtonListDialogScreen;
import net.mayaan.client.gui.screens.dialog.DialogConnectionAccess;
import net.mayaan.core.Holder;
import net.mayaan.network.chat.ClickEvent;
import net.mayaan.server.dialog.ActionButton;
import net.mayaan.server.dialog.CommonButtonData;
import net.mayaan.server.dialog.Dialog;
import net.mayaan.server.dialog.DialogListDialog;
import net.mayaan.server.dialog.action.StaticAction;
import org.jspecify.annotations.Nullable;

public class DialogListDialogScreen
extends ButtonListDialogScreen<DialogListDialog> {
    public DialogListDialogScreen(@Nullable Screen previousScreen, DialogListDialog dialog, DialogConnectionAccess connectionAccess) {
        super(previousScreen, dialog, connectionAccess);
    }

    @Override
    protected Stream<ActionButton> createListActions(DialogListDialog data, DialogConnectionAccess connectionAccess) {
        return data.dialogs().stream().map(subDialog -> DialogListDialogScreen.createDialogClickAction(data, subDialog));
    }

    private static ActionButton createDialogClickAction(DialogListDialog data, Holder<Dialog> subDialog) {
        return new ActionButton(new CommonButtonData(subDialog.value().common().computeExternalTitle(), data.buttonWidth()), Optional.of(new StaticAction(new ClickEvent.ShowDialog(subDialog))));
    }
}

