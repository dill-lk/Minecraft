/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.options;

import net.mayaan.client.OptionInstance;
import net.mayaan.client.Options;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.options.OptionsSubScreen;
import net.mayaan.network.chat.Component;

public class FontOptionsScreen
extends OptionsSubScreen {
    private static OptionInstance<?>[] options(Options options) {
        return new OptionInstance[]{options.forceUnicodeFont(), options.japaneseGlyphVariants()};
    }

    public FontOptionsScreen(Screen lastScreen, Options options) {
        super(lastScreen, options, (Component)Component.translatable("options.font.title"));
    }

    @Override
    protected void addOptions() {
        this.list.addSmall(FontOptionsScreen.options(this.options));
    }
}

