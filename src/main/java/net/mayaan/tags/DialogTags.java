/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.tags;

import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.server.dialog.Dialog;
import net.mayaan.tags.TagKey;

public class DialogTags {
    public static final TagKey<Dialog> PAUSE_SCREEN_ADDITIONS = DialogTags.create("pause_screen_additions");
    public static final TagKey<Dialog> QUICK_ACTIONS = DialogTags.create("quick_actions");

    private DialogTags() {
    }

    private static TagKey<Dialog> create(String name) {
        return TagKey.create(Registries.DIALOG, Identifier.withDefaultNamespace(name));
    }
}

