/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.multiplayer;

import net.mayaan.ChatFormatting;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.layouts.Layout;
import net.mayaan.client.gui.layouts.LinearLayout;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.mayaan.client.gui.screens.multiplayer.WarningScreen;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;

public class SafetyScreen
extends WarningScreen {
    private static final Component TITLE = Component.translatable("multiplayerWarning.header").withStyle(ChatFormatting.BOLD);
    private static final Component CONTENT = Component.translatable("multiplayerWarning.message");
    private static final Component CHECK = Component.translatable("multiplayerWarning.check").withColor(-2039584);
    private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);
    private final Screen previous;

    public SafetyScreen(Screen previous) {
        super(TITLE, CONTENT, CHECK, NARRATION);
        this.previous = previous;
    }

    @Override
    protected Layout addFooterButtons() {
        LinearLayout footer = LinearLayout.horizontal().spacing(8);
        footer.addChild(Button.builder(CommonComponents.GUI_PROCEED, button -> {
            if (this.stopShowing.selected()) {
                this.minecraft.options.skipMultiplayerWarning = true;
                this.minecraft.options.save();
            }
            this.minecraft.setScreen(new JoinMultiplayerScreen(this.previous));
        }).build());
        footer.addChild(Button.builder(CommonComponents.GUI_BACK, button -> this.onClose()).build());
        return footer;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.previous);
    }
}

