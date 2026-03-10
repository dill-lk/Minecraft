/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.server.dialog;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.Registry;
import net.mayaan.server.dialog.ConfirmationDialog;
import net.mayaan.server.dialog.Dialog;
import net.mayaan.server.dialog.DialogListDialog;
import net.mayaan.server.dialog.MultiActionDialog;
import net.mayaan.server.dialog.NoticeDialog;
import net.mayaan.server.dialog.ServerLinksDialog;

public class DialogTypes {
    public static MapCodec<? extends Dialog> bootstrap(Registry<MapCodec<? extends Dialog>> registry) {
        Registry.register(registry, "notice", NoticeDialog.MAP_CODEC);
        Registry.register(registry, "server_links", ServerLinksDialog.MAP_CODEC);
        Registry.register(registry, "dialog_list", DialogListDialog.MAP_CODEC);
        Registry.register(registry, "multi_action", MultiActionDialog.MAP_CODEC);
        return Registry.register(registry, "confirmation", ConfirmationDialog.MAP_CODEC);
    }
}

