/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.options;

import com.maayanlabs.blaze3d.platform.InputConstants;
import java.util.Arrays;
import java.util.stream.Stream;
import net.mayaan.client.OptionInstance;
import net.mayaan.client.Options;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.options.OptionsSubScreen;
import net.mayaan.network.chat.Component;

public class MouseSettingsScreen
extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("options.mouse_settings.title");

    private static OptionInstance<?>[] options(Options options) {
        return new OptionInstance[]{options.sensitivity(), options.touchscreen(), options.mouseWheelSensitivity(), options.discreteMouseScroll(), options.invertMouseX(), options.invertMouseY(), options.allowCursorChanges()};
    }

    public MouseSettingsScreen(Screen lastScreen, Options options) {
        super(lastScreen, options, TITLE);
    }

    @Override
    protected void addOptions() {
        if (InputConstants.isRawMouseInputSupported()) {
            this.list.addSmall((OptionInstance[])Stream.concat(Arrays.stream(MouseSettingsScreen.options(this.options)), Stream.of(this.options.rawMouseInput())).toArray(OptionInstance[]::new));
        } else {
            this.list.addSmall(MouseSettingsScreen.options(this.options));
        }
    }
}

