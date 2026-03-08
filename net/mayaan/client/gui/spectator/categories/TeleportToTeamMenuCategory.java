/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.spectator.categories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.PlayerFaceRenderer;
import net.mayaan.client.gui.spectator.SpectatorMenu;
import net.mayaan.client.gui.spectator.SpectatorMenuCategory;
import net.mayaan.client.gui.spectator.SpectatorMenuItem;
import net.mayaan.client.gui.spectator.categories.TeleportToPlayerMenuCategory;
import net.mayaan.client.multiplayer.PlayerInfo;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.util.ARGB;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.player.PlayerSkin;
import net.mayaan.world.level.GameType;
import net.mayaan.world.scores.PlayerTeam;
import net.mayaan.world.scores.Scoreboard;

public class TeleportToTeamMenuCategory
implements SpectatorMenuCategory,
SpectatorMenuItem {
    private static final Identifier TELEPORT_TO_TEAM_SPRITE = Identifier.withDefaultNamespace("spectator/teleport_to_team");
    private static final Component TELEPORT_TEXT = Component.translatable("spectatorMenu.team_teleport");
    private static final Component TELEPORT_PROMPT = Component.translatable("spectatorMenu.team_teleport.prompt");
    private final List<SpectatorMenuItem> items;

    public TeleportToTeamMenuCategory() {
        Mayaan minecraft = Mayaan.getInstance();
        this.items = TeleportToTeamMenuCategory.createTeamEntries(minecraft, minecraft.level.getScoreboard());
    }

    private static List<SpectatorMenuItem> createTeamEntries(Mayaan minecraft, Scoreboard scoreboard) {
        return scoreboard.getPlayerTeams().stream().flatMap(team -> TeamSelectionItem.create(minecraft, team).stream()).toList();
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
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TELEPORT_TO_TEAM_SPRITE, 0, 0, 16, 16, ARGB.colorFromFloat(alpha, brightness, brightness, brightness));
    }

    @Override
    public boolean isEnabled() {
        return !this.items.isEmpty();
    }

    private static class TeamSelectionItem
    implements SpectatorMenuItem {
        private final PlayerTeam team;
        private final Supplier<PlayerSkin> iconSkin;
        private final List<PlayerInfo> players;

        private TeamSelectionItem(PlayerTeam team, List<PlayerInfo> players, Supplier<PlayerSkin> iconSkin) {
            this.team = team;
            this.players = players;
            this.iconSkin = iconSkin;
        }

        public static Optional<SpectatorMenuItem> create(Mayaan minecraft, PlayerTeam team) {
            ArrayList<PlayerInfo> players = new ArrayList<PlayerInfo>();
            for (String name : team.getPlayers()) {
                PlayerInfo info = minecraft.getConnection().getPlayerInfo(name);
                if (info == null || info.getGameMode() == GameType.SPECTATOR) continue;
                players.add(info);
            }
            if (players.isEmpty()) {
                return Optional.empty();
            }
            PlayerInfo playerInfo = (PlayerInfo)players.get(RandomSource.createThreadLocalInstance().nextInt(players.size()));
            return Optional.of(new TeamSelectionItem(team, players, playerInfo::getSkin));
        }

        @Override
        public void selectItem(SpectatorMenu menu) {
            menu.selectCategory(new TeleportToPlayerMenuCategory(this.players));
        }

        @Override
        public Component getName() {
            return this.team.getDisplayName();
        }

        @Override
        public void renderIcon(GuiGraphics graphics, float brightness, float alpha) {
            Integer teamColor = this.team.getColor().getColor();
            if (teamColor != null) {
                float red = (float)(teamColor >> 16 & 0xFF) / 255.0f;
                float green = (float)(teamColor >> 8 & 0xFF) / 255.0f;
                float blue = (float)(teamColor & 0xFF) / 255.0f;
                graphics.fill(1, 1, 15, 15, ARGB.colorFromFloat(alpha, red * brightness, green * brightness, blue * brightness));
            }
            PlayerFaceRenderer.draw(graphics, this.iconSkin.get(), 2, 2, 12, ARGB.colorFromFloat(alpha, brightness, brightness, brightness));
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}

