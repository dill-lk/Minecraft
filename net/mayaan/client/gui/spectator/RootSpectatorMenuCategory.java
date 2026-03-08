/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.mayaan.client.gui.spectator;

import com.google.common.collect.Lists;
import java.util.List;
import net.mayaan.client.gui.spectator.SpectatorMenuCategory;
import net.mayaan.client.gui.spectator.SpectatorMenuItem;
import net.mayaan.client.gui.spectator.categories.TeleportToPlayerMenuCategory;
import net.mayaan.client.gui.spectator.categories.TeleportToTeamMenuCategory;
import net.mayaan.network.chat.Component;

public class RootSpectatorMenuCategory
implements SpectatorMenuCategory {
    private static final Component PROMPT_TEXT = Component.translatable("spectatorMenu.root.prompt");
    private final List<SpectatorMenuItem> items = Lists.newArrayList();

    public RootSpectatorMenuCategory() {
        this.items.add(new TeleportToPlayerMenuCategory());
        this.items.add(new TeleportToTeamMenuCategory());
    }

    @Override
    public List<SpectatorMenuItem> getItems() {
        return this.items;
    }

    @Override
    public Component getPrompt() {
        return PROMPT_TEXT;
    }
}

