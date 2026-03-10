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
import net.mayaan.network.chat.ClickEvent;
import net.mayaan.server.ServerLinks;
import net.mayaan.server.dialog.ActionButton;
import net.mayaan.server.dialog.CommonButtonData;
import net.mayaan.server.dialog.ServerLinksDialog;
import net.mayaan.server.dialog.action.StaticAction;
import org.jspecify.annotations.Nullable;

public class ServerLinksDialogScreen
extends ButtonListDialogScreen<ServerLinksDialog> {
    public ServerLinksDialogScreen(@Nullable Screen previousScreen, ServerLinksDialog dialog, DialogConnectionAccess connectionAccess) {
        super(previousScreen, dialog, connectionAccess);
    }

    @Override
    protected Stream<ActionButton> createListActions(ServerLinksDialog dialog, DialogConnectionAccess connectionAccess) {
        return connectionAccess.serverLinks().entries().stream().map(entry -> ServerLinksDialogScreen.createDialogClickAction(dialog, entry));
    }

    private static ActionButton createDialogClickAction(ServerLinksDialog data, ServerLinks.Entry entry) {
        return new ActionButton(new CommonButtonData(entry.displayName(), data.buttonWidth()), Optional.of(new StaticAction(new ClickEvent.OpenUrl(entry.link()))));
    }
}

