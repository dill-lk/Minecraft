/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.spectator;

import java.util.List;
import net.mayaan.client.gui.spectator.SpectatorMenuItem;
import net.mayaan.network.chat.Component;

public interface SpectatorMenuCategory {
    public List<SpectatorMenuItem> getItems();

    public Component getPrompt();
}

