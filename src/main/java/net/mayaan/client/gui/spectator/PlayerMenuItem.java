/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.spectator;

import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.PlayerFaceRenderer;
import net.mayaan.client.gui.spectator.SpectatorMenu;
import net.mayaan.client.gui.spectator.SpectatorMenuItem;
import net.mayaan.client.multiplayer.PlayerInfo;
import net.mayaan.network.chat.Component;
import net.mayaan.network.protocol.game.ServerboundTeleportToEntityPacket;
import net.mayaan.util.ARGB;

public class PlayerMenuItem
implements SpectatorMenuItem {
    private final PlayerInfo playerInfo;
    private final Component name;

    public PlayerMenuItem(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        this.name = Component.literal(playerInfo.getProfile().name());
    }

    @Override
    public void selectItem(SpectatorMenu menu) {
        Mayaan.getInstance().getConnection().send(new ServerboundTeleportToEntityPacket(this.playerInfo.getProfile().id()));
    }

    @Override
    public Component getName() {
        return this.name;
    }

    @Override
    public void renderIcon(GuiGraphics graphics, float brightness, float alpha) {
        PlayerFaceRenderer.draw(graphics, this.playerInfo.getSkin(), 2, 2, 12, ARGB.white(alpha));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

