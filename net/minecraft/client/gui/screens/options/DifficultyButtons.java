/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui.screens.options;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.LockIconButton;
import net.minecraft.client.gui.layouts.EqualSpacingLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
import net.minecraft.world.Difficulty;

public class DifficultyButtons {
    public static LayoutElement create(Minecraft minecraft, Screen screen) {
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

    private static boolean isDifficultyLocked(Minecraft minecraft) {
        return minecraft.level.getLevelData().isDifficultyLocked() || minecraft.level.getLevelData().isHardcore();
    }

    private static boolean playerHasPermissionToChangeDifficulty(Minecraft minecraft) {
        return minecraft.hasSingleplayerServer();
    }

    private static void onLockCallback(boolean result, Minecraft minecraft, Screen screen, CycleButton<Difficulty> difficultyButton, LockIconButton lockButton) {
        minecraft.setScreen(screen);
        if (result && minecraft.level != null) {
            minecraft.getConnection().send(new ServerboundLockDifficultyPacket(true));
            lockButton.setLocked(true);
            lockButton.active = false;
            difficultyButton.active = false;
        }
    }
}

