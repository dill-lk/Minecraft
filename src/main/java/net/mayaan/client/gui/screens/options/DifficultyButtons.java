/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.options;

import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.CycleButton;
import net.mayaan.client.gui.components.LockIconButton;
import net.mayaan.client.gui.layouts.EqualSpacingLayout;
import net.mayaan.client.gui.layouts.LayoutElement;
import net.mayaan.client.gui.screens.ConfirmScreen;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.network.chat.Component;
import net.mayaan.network.protocol.game.ServerboundChangeDifficultyPacket;
import net.mayaan.network.protocol.game.ServerboundLockDifficultyPacket;
import net.mayaan.world.Difficulty;

public class DifficultyButtons {
    public static LayoutElement create(Mayaan minecraft, Screen screen) {
        CycleButton<Difficulty> difficultyButton = CycleButton.builder(Difficulty::getDisplayName, minecraft.level.getDifficulty()).withValues((Difficulty[])Difficulty.values()).create(0, 0, 150, 20, Component.translatable("options.difficulty"), (button, value) -> minecraft.getConnection().send(new ServerboundChangeDifficultyPacket((Difficulty)value)));
        LockIconButton lockButton = new LockIconButton(0, 0, button -> minecraft.setScreen(new ConfirmScreen(result -> DifficultyButtons.onLockCallback(result, minecraft, screen, difficultyButton, (LockIconButton)button), Component.translatable("difficulty.lock.title"), (Component)Component.translatable("difficulty.lock.question", minecraft.level.getLevelData().getDifficulty().getDisplayName()))));
        difficultyButton.setWidth(difficultyButton.getWidth() - lockButton.getWidth());
        lockButton.setLocked(DifficultyButtons.isDifficultyLocked(minecraft));
        lockButton.active = !lockButton.isLocked() && DifficultyButtons.playerHasPermissionToChangeDifficulty(minecraft);
        difficultyButton.active = !lockButton.isLocked() && DifficultyButtons.playerHasPermissionToChangeDifficulty(minecraft);
        EqualSpacingLayout linearLayout = new EqualSpacingLayout(150, 0, EqualSpacingLayout.Orientation.HORIZONTAL);
        linearLayout.addChild(difficultyButton);
        linearLayout.addChild(lockButton);
        return linearLayout;
    }

    private static boolean isDifficultyLocked(Mayaan minecraft) {
        return minecraft.level.getLevelData().isDifficultyLocked() || minecraft.level.getLevelData().isHardcore();
    }

    private static boolean playerHasPermissionToChangeDifficulty(Mayaan minecraft) {
        return minecraft.hasSingleplayerServer();
    }

    private static void onLockCallback(boolean result, Mayaan minecraft, Screen screen, CycleButton<Difficulty> difficultyButton, LockIconButton lockButton) {
        minecraft.setScreen(screen);
        if (result && minecraft.level != null) {
            minecraft.getConnection().send(new ServerboundLockDifficultyPacket(true));
            lockButton.setLocked(true);
            lockButton.active = false;
            difficultyButton.active = false;
        }
    }
}

