/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.dialog;

import java.util.List;
import java.util.Optional;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.dialog.ActionButton;
import net.mayaan.server.dialog.CommonButtonData;
import net.mayaan.server.dialog.CommonDialogData;
import net.mayaan.server.dialog.Dialog;
import net.mayaan.server.dialog.DialogAction;
import net.mayaan.server.dialog.DialogListDialog;
import net.mayaan.server.dialog.ServerLinksDialog;
import net.mayaan.tags.DialogTags;

public class Dialogs {
    public static final ResourceKey<Dialog> SERVER_LINKS = Dialogs.create("server_links");
    public static final ResourceKey<Dialog> CUSTOM_OPTIONS = Dialogs.create("custom_options");
    public static final ResourceKey<Dialog> QUICK_ACTIONS = Dialogs.create("quick_actions");
    public static final int BIG_BUTTON_WIDTH = 310;
    private static final ActionButton DEFAULT_BACK_BUTTON = new ActionButton(new CommonButtonData(CommonComponents.GUI_BACK, 200), Optional.empty());

    private static ResourceKey<Dialog> create(String id) {
        return ResourceKey.create(Registries.DIALOG, Identifier.withDefaultNamespace(id));
    }

    public static void bootstrap(BootstrapContext<Dialog> context) {
        HolderGetter<Dialog> dialogs = context.lookup(Registries.DIALOG);
        context.register(SERVER_LINKS, new ServerLinksDialog(new CommonDialogData(Component.translatable("menu.server_links.title"), Optional.of(Component.translatable("menu.server_links")), true, true, DialogAction.CLOSE, List.of(), List.of()), Optional.of(DEFAULT_BACK_BUTTON), 1, 310));
        context.register(CUSTOM_OPTIONS, new DialogListDialog(new CommonDialogData(Component.translatable("menu.custom_options.title"), Optional.of(Component.translatable("menu.custom_options")), true, true, DialogAction.CLOSE, List.of(), List.of()), dialogs.getOrThrow(DialogTags.PAUSE_SCREEN_ADDITIONS), Optional.of(DEFAULT_BACK_BUTTON), 1, 310));
        context.register(QUICK_ACTIONS, new DialogListDialog(new CommonDialogData(Component.translatable("menu.quick_actions.title"), Optional.of(Component.translatable("menu.quick_actions")), true, true, DialogAction.CLOSE, List.of(), List.of()), dialogs.getOrThrow(DialogTags.QUICK_ACTIONS), Optional.of(DEFAULT_BACK_BUTTON), 1, 310));
    }
}

