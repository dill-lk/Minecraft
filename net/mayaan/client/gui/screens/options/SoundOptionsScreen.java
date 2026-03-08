/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.options;

import java.util.Arrays;
import net.mayaan.client.OptionInstance;
import net.mayaan.client.Options;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.options.OptionsSubScreen;
import net.mayaan.network.chat.Component;
import net.mayaan.sounds.SoundSource;

public class SoundOptionsScreen
extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("options.sounds.title");

    public SoundOptionsScreen(Screen lastScreen, Options options) {
        super(lastScreen, options, TITLE);
    }

    @Override
    protected void addOptions() {
        this.list.addBig(this.options.getSoundSourceOptionInstance(SoundSource.MASTER));
        this.list.addSmall(this.getAllSoundOptionsExceptMaster());
        this.list.addBig(this.options.soundDevice());
        this.list.addSmall(this.options.showSubtitles(), this.options.directionalAudio());
        this.list.addSmall(this.options.musicFrequency(), this.options.musicToast());
    }

    private OptionInstance<?>[] getAllSoundOptionsExceptMaster() {
        return (OptionInstance[])Arrays.stream(SoundSource.values()).filter(s -> s != SoundSource.MASTER).map(this.options::getSoundSourceOptionInstance).toArray(OptionInstance[]::new);
    }
}

