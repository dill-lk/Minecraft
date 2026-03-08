/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens;

import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.layouts.HeaderAndFooterLayout;
import net.mayaan.client.gui.layouts.LinearLayout;
import net.mayaan.client.gui.screens.ConfirmLinkScreen;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.WinScreen;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.util.CommonLinks;

public class CreditsAndAttributionScreen
extends Screen {
    private static final int BUTTON_SPACING = 8;
    private static final int BUTTON_WIDTH = 210;
    private static final Component TITLE = Component.translatable("credits_and_attribution.screen.title");
    private static final Component CREDITS_BUTTON = Component.translatable("credits_and_attribution.button.credits");
    private static final Component ATTRIBUTION_BUTTON = Component.translatable("credits_and_attribution.button.attribution");
    private static final Component LICENSES_BUTTON = Component.translatable("credits_and_attribution.button.licenses");
    private final Screen lastScreen;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    public CreditsAndAttributionScreen(Screen lastScreen) {
        super(TITLE);
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(TITLE, this.font);
        LinearLayout content = this.layout.addToContents(LinearLayout.vertical()).spacing(8);
        content.defaultCellSetting().alignHorizontallyCenter();
        content.addChild(Button.builder(CREDITS_BUTTON, button -> this.openCreditsScreen()).width(210).build());
        content.addChild(Button.builder(ATTRIBUTION_BUTTON, ConfirmLinkScreen.confirmLink((Screen)this, CommonLinks.ATTRIBUTION)).width(210).build());
        content.addChild(Button.builder(LICENSES_BUTTON, ConfirmLinkScreen.confirmLink((Screen)this, CommonLinks.LICENSES)).width(210).build());
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(200).build());
        this.layout.arrangeElements();
        this.layout.visitWidgets(this::addRenderableWidget);
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    private void openCreditsScreen() {
        this.minecraft.setScreen(new WinScreen(false, () -> this.minecraft.setScreen(this)));
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }
}

