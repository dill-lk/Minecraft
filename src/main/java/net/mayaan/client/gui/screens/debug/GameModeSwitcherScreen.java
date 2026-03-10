/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  java.lang.MatchException
 */
package net.mayaan.client.gui.screens.debug;

import com.google.common.collect.Lists;
import java.util.List;
import net.mayaan.ChatFormatting;
import net.mayaan.client.GameNarrator;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.narration.NarrationElementOutput;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.input.KeyEvent;
import net.mayaan.client.input.MouseButtonEvent;
import net.mayaan.client.multiplayer.MultiPlayerGameMode;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.network.protocol.game.ServerboundChangeGameModePacket;
import net.mayaan.resources.Identifier;
import net.mayaan.server.commands.GameModeCommand;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.GameType;
import net.mayaan.world.level.block.Blocks;

public class GameModeSwitcherScreen
extends Screen {
    private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("gamemode_switcher/slot");
    private static final Identifier SELECTION_SPRITE = Identifier.withDefaultNamespace("gamemode_switcher/selection");
    private static final Identifier GAMEMODE_SWITCHER_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/gamemode_switcher.png");
    private static final int SPRITE_SHEET_WIDTH = 128;
    private static final int SPRITE_SHEET_HEIGHT = 128;
    private static final int SLOT_AREA = 26;
    private static final int SLOT_PADDING = 5;
    private static final int SLOT_AREA_PADDED = 31;
    private static final int HELP_TIPS_OFFSET_Y = 5;
    private static final int ALL_SLOTS_WIDTH = GameModeIcon.values().length * 31 - 5;
    private final GameModeIcon previousHovered;
    private GameModeIcon currentlyHovered;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    private final List<GameModeSlot> slots = Lists.newArrayList();

    public GameModeSwitcherScreen() {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = this.previousHovered = GameModeIcon.getFromGameType(this.getDefaultSelected());
    }

    private GameType getDefaultSelected() {
        MultiPlayerGameMode gameMode = Mayaan.getInstance().gameMode;
        GameType previous = gameMode.getPreviousPlayerMode();
        if (previous != null) {
            return previous;
        }
        return gameMode.getPlayerMode() == GameType.CREATIVE ? GameType.SURVIVAL : GameType.CREATIVE;
    }

    @Override
    protected void init() {
        super.init();
        this.slots.clear();
        this.currentlyHovered = this.previousHovered;
        for (int i = 0; i < GameModeIcon.VALUES.length; ++i) {
            GameModeIcon icon = GameModeIcon.VALUES[i];
            this.slots.add(new GameModeSlot(icon, this.width / 2 - ALL_SLOTS_WIDTH / 2 + i * 31, this.height / 2 - 31));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        graphics.drawCenteredString(this.font, this.currentlyHovered.name, this.width / 2, this.height / 2 - 31 - 20, -1);
        MutableComponent selectKey = Component.translatable("debug.gamemodes.select_next", this.minecraft.options.keyDebugSwitchGameMode.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.AQUA));
        graphics.drawCenteredString(this.font, selectKey, this.width / 2, this.height / 2 + 5, -1);
        if (!this.setFirstMousePos) {
            this.firstMouseX = mouseX;
            this.firstMouseY = mouseY;
            this.setFirstMousePos = true;
        }
        boolean sameAsFirstMousePos = this.firstMouseX == mouseX && this.firstMouseY == mouseY;
        for (GameModeSlot slot : this.slots) {
            slot.render(graphics, mouseX, mouseY, a);
            slot.setSelected(this.currentlyHovered == slot.icon);
            if (sameAsFirstMousePos || !slot.isHoveredOrFocused()) continue;
            this.currentlyHovered = slot.icon;
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        int xo = this.width / 2 - 62;
        int yo = this.height / 2 - 31 - 27;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GAMEMODE_SWITCHER_LOCATION, xo, yo, 0.0f, 0.0f, 125, 75, 128, 128);
    }

    private void switchToHoveredGameMode() {
        GameModeSwitcherScreen.switchToHoveredGameMode(this.minecraft, this.currentlyHovered);
    }

    private static void switchToHoveredGameMode(Mayaan minecraft, GameModeIcon toGameMode) {
        if (!minecraft.canSwitchGameMode()) {
            return;
        }
        GameModeIcon currentGameMode = GameModeIcon.getFromGameType(minecraft.gameMode.getPlayerMode());
        if (toGameMode != currentGameMode && GameModeCommand.PERMISSION_CHECK.check(minecraft.player.permissions())) {
            minecraft.player.connection.send(new ServerboundChangeGameModePacket(toGameMode.mode));
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.minecraft.options.keyDebugSwitchGameMode.matches(event)) {
            this.setFirstMousePos = false;
            this.currentlyHovered = this.currentlyHovered.getNext();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        if (this.minecraft.options.keyDebugModifier.matches(event)) {
            this.switchToHoveredGameMode();
            this.minecraft.setScreen(null);
            return true;
        }
        return super.keyReleased(event);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (this.minecraft.options.keyDebugModifier.matchesMouse(event)) {
            this.switchToHoveredGameMode();
            this.minecraft.setScreen(null);
            return true;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static enum GameModeIcon {
        CREATIVE(Component.translatable("gameMode.creative"), GameType.CREATIVE, new ItemStack(Blocks.GRASS_BLOCK)),
        SURVIVAL(Component.translatable("gameMode.survival"), GameType.SURVIVAL, new ItemStack(Items.IRON_SWORD)),
        ADVENTURE(Component.translatable("gameMode.adventure"), GameType.ADVENTURE, new ItemStack(Items.MAP)),
        SPECTATOR(Component.translatable("gameMode.spectator"), GameType.SPECTATOR, new ItemStack(Items.ENDER_EYE));

        private static final GameModeIcon[] VALUES;
        private static final int ICON_AREA = 16;
        private static final int ICON_TOP_LEFT = 5;
        private final Component name;
        private final GameType mode;
        private final ItemStack renderStack;

        private GameModeIcon(Component name, GameType mode, ItemStack renderStack) {
            this.name = name;
            this.mode = mode;
            this.renderStack = renderStack;
        }

        private void drawIcon(GuiGraphics graphics, int x, int y) {
            graphics.renderItem(this.renderStack, x, y);
        }

        private GameModeIcon getNext() {
            return switch (this.ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> SURVIVAL;
                case 1 -> ADVENTURE;
                case 2 -> SPECTATOR;
                case 3 -> CREATIVE;
            };
        }

        private static GameModeIcon getFromGameType(GameType gameType) {
            return switch (gameType) {
                default -> throw new MatchException(null, null);
                case GameType.SPECTATOR -> SPECTATOR;
                case GameType.SURVIVAL -> SURVIVAL;
                case GameType.CREATIVE -> CREATIVE;
                case GameType.ADVENTURE -> ADVENTURE;
            };
        }

        static {
            VALUES = GameModeIcon.values();
        }
    }

    public static class GameModeSlot
    extends AbstractWidget {
        private final GameModeIcon icon;
        private boolean isSelected;

        public GameModeSlot(GameModeIcon icon, int x, int y) {
            super(x, y, 26, 26, icon.name);
            this.icon = icon;
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
            this.drawSlot(graphics);
            if (this.isSelected) {
                this.drawSelection(graphics);
            }
            this.icon.drawIcon(graphics, this.getX() + 5, this.getY() + 5);
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput output) {
            this.defaultButtonNarrationText(output);
        }

        @Override
        public boolean isHoveredOrFocused() {
            return super.isHoveredOrFocused() || this.isSelected;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        private void drawSlot(GuiGraphics graphics) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_SPRITE, this.getX(), this.getY(), 26, 26);
        }

        private void drawSelection(GuiGraphics graphics) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SELECTION_SPRITE, this.getX(), this.getY(), 26, 26);
        }
    }
}

