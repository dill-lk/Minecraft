/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.tags;

import java.util.concurrent.CompletableFuture;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.PackOutput;
import net.mayaan.data.tags.KeyTagProvider;
import net.mayaan.server.dialog.Dialog;
import net.mayaan.tags.DialogTags;

public class DialogTagsProvider
extends KeyTagProvider<Dialog> {
    public DialogTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.DIALOG, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.tag(DialogTags.PAUSE_SCREEN_ADDITIONS);
        this.tag(DialogTags.QUICK_ACTIONS);
    }
}

