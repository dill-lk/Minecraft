/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.dialog;

import com.mojang.serialization.MapCodec;
import java.util.HashMap;
import java.util.Map;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.dialog.DialogConnectionAccess;
import net.mayaan.client.gui.screens.dialog.DialogListDialogScreen;
import net.mayaan.client.gui.screens.dialog.DialogScreen;
import net.mayaan.client.gui.screens.dialog.MultiButtonDialogScreen;
import net.mayaan.client.gui.screens.dialog.ServerLinksDialogScreen;
import net.mayaan.client.gui.screens.dialog.SimpleDialogScreen;
import net.mayaan.server.dialog.ConfirmationDialog;
import net.mayaan.server.dialog.Dialog;
import net.mayaan.server.dialog.DialogListDialog;
import net.mayaan.server.dialog.MultiActionDialog;
import net.mayaan.server.dialog.NoticeDialog;
import net.mayaan.server.dialog.ServerLinksDialog;
import org.jspecify.annotations.Nullable;

public class DialogScreens {
    private static final Map<MapCodec<? extends Dialog>, Factory<?>> FACTORIES = new HashMap();

    private static <T extends Dialog> void register(MapCodec<T> type, Factory<? super T> factory) {
        FACTORIES.put(type, factory);
    }

    public static <T extends Dialog> @Nullable DialogScreen<T> createFromData(T dialog, @Nullable Screen previousScreen, DialogConnectionAccess connectionAccess) {
        Factory<?> factory = FACTORIES.get(dialog.codec());
        if (factory != null) {
            return factory.create(previousScreen, dialog, connectionAccess);
        }
        return null;
    }

    public static void bootstrap() {
        DialogScreens.register(ConfirmationDialog.MAP_CODEC, SimpleDialogScreen::new);
        DialogScreens.register(NoticeDialog.MAP_CODEC, SimpleDialogScreen::new);
        DialogScreens.register(DialogListDialog.MAP_CODEC, DialogListDialogScreen::new);
        DialogScreens.register(MultiActionDialog.MAP_CODEC, MultiButtonDialogScreen::new);
        DialogScreens.register(ServerLinksDialog.MAP_CODEC, ServerLinksDialogScreen::new);
    }

    @FunctionalInterface
    public static interface Factory<T extends Dialog> {
        public DialogScreen<T> create(@Nullable Screen var1, T var2, DialogConnectionAccess var3);
    }
}

