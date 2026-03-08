/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.options;

import net.mayaan.client.OptionInstance;
import net.mayaan.client.Options;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.options.OptionsSubScreen;
import net.mayaan.network.chat.Component;

public class ChatOptionsScreen
extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("options.chat.title");

    private static OptionInstance<?>[] options(Options options) {
        return new OptionInstance[]{options.chatVisibility(), options.chatColors(), options.chatLinks(), options.chatLinksPrompt(), options.chatOpacity(), options.textBackgroundOpacity(), options.chatScale(), options.chatLineSpacing(), options.chatDelay(), options.chatWidth(), options.chatHeightFocused(), options.chatHeightUnfocused(), options.narrator(), options.autoSuggestions(), options.hideMatchedNames(), options.reducedDebugInfo(), options.onlyShowSecureChat(), options.saveChatDrafts()};
    }

    public ChatOptionsScreen(Screen lastScreen, Options options) {
        super(lastScreen, options, TITLE);
    }

    @Override
    protected void addOptions() {
        this.list.addSmall(ChatOptionsScreen.options(this.options));
    }
}

