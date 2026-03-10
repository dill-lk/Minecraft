/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.options.controls;

import net.mayaan.client.OptionInstance;
import net.mayaan.client.Options;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.options.MouseSettingsScreen;
import net.mayaan.client.gui.screens.options.OptionsSubScreen;
import net.mayaan.client.gui.screens.options.controls.KeyBindsScreen;
import net.mayaan.network.chat.Component;

public class ControlsScreen
extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("controls.title");

    private static OptionInstance<?>[] options(Options options) {
        return new OptionInstance[]{options.toggleCrouch(), options.toggleSprint(), options.toggleAttack(), options.toggleUse(), options.autoJump(), options.sprintWindow(), options.operatorItemsTab()};
    }

    public ControlsScreen(Screen lastScreen, Options options) {
        super(lastScreen, options, TITLE);
    }

    @Override
    protected void addOptions() {
        this.list.addSmall(Button.builder(Component.translatable("options.mouse_settings"), button -> this.minecraft.setScreen(new MouseSettingsScreen(this, this.options))).build(), Button.builder(Component.translatable("controls.keybinds"), button -> this.minecraft.setScreen(new KeyBindsScreen(this, this.options))).build());
        this.list.addSmall(ControlsScreen.options(this.options));
    }
}

