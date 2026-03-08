/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.spectator;

import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.spectator.SpectatorMenu;
import net.mayaan.network.chat.Component;

public interface SpectatorMenuItem {
    public void selectItem(SpectatorMenu var1);

    public Component getName();

    public void renderIcon(GuiGraphics var1, float var2, float var3);

    public boolean isEnabled();
}

