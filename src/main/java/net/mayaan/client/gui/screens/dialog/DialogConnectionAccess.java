/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.dialog;

import java.util.Optional;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.core.Holder;
import net.mayaan.nbt.Tag;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.server.ServerLinks;
import net.mayaan.server.dialog.Dialog;
import org.jspecify.annotations.Nullable;

public interface DialogConnectionAccess {
    public void disconnect(Component var1);

    public void runCommand(String var1, @Nullable Screen var2);

    public void openDialog(Holder<Dialog> var1, @Nullable Screen var2);

    public void sendCustomAction(Identifier var1, Optional<Tag> var2);

    public ServerLinks serverLinks();
}

