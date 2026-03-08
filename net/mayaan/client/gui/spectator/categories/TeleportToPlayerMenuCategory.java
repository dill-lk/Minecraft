/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.spectator.categories;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.spectator.PlayerMenuItem;
import net.mayaan.client.gui.spectator.SpectatorMenu;
import net.mayaan.client.gui.spectator.SpectatorMenuCategory;
import net.mayaan.client.gui.spectator.SpectatorMenuItem;
import net.mayaan.client.multiplayer.PlayerInfo;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.util.ARGB;
import net.mayaan.world.level.GameType;

public class TeleportToPlayerMenuCategory
implements SpectatorMenuCategory,
SpectatorMenuItem {
    private static final Identifier TELEPORT_TO_PLAYER_SPRITE = Identifier.withDefaultNamespace("spectator/teleport_to_player");
    private static final Comparator<PlayerInfo> PROFILE_ORDER = Comparator.comparing(p -> p.getProfile().id());
    private static final Component TELEPORT_TEXT = Component.translatable("spectatorMenu.teleport");
    private static final Component TELEPORT_PROMPT = Component.translatable("spectatorMenu.teleport.prompt");
    private final List<SpectatorMenuItem> items;

    public TeleportToPlayerMenuCategory() {
        this(Mayaan.getInstance().getConnection().getListedOnlinePlayers());
    }

    public TeleportToPlayerMenuCategory(Collection<PlayerInfo> profiles) {
        this.items = profiles.stream().filter(p -> p.getGameMode() != GameType.SPECTATOR).sorted(PROFILE_ORDER).map(PlayerMenuItem::new).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<SpectatorMenuItem> getItems() {
        return this.items;
    }

    @Override
    public Component getPrompt() {
        return TELEPORT_PROMPT;
    }

    @Override
    public void selectItem(SpectatorMenu menu) {
        menu.selectCategory(this);
    }

    @Override
    public Component getName() {
        return TELEPORT_TEXT;
    }

    @Override
    public void renderIcon(GuiGraphics graphics, float brightness, float alpha) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TELEPORT_TO_PLAYER_SPRITE, 0, 0, 16, 16, ARGB.colorFromFloat(alpha, brightness, brightness, brightness));
    }

    @Override
    public boolean isEnabled() {
        return !this.items.isEmpty();
    }
}

