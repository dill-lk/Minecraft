/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package com.maayanlabs.realmsclient.gui.screens;

import net.mayaan.client.GameNarrator;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.MultiLineTextWidget;
import net.mayaan.client.gui.layouts.FrameLayout;
import net.mayaan.client.gui.layouts.LinearLayout;
import net.mayaan.client.gui.screens.ConfirmLinkScreen;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.realms.RealmsScreen;
import net.mayaan.util.CommonLinks;
import org.jspecify.annotations.Nullable;

public class RealmsParentalConsentScreen
extends RealmsScreen {
    private static final Component MESSAGE = Component.translatable("mco.account.privacy.information");
    private static final int SPACING = 15;
    private final LinearLayout layout = LinearLayout.vertical();
    private final Screen lastScreen;
    private @Nullable MultiLineTextWidget textWidget;

    public RealmsParentalConsentScreen(Screen lastScreen) {
        super(GameNarrator.NO_TITLE);
        this.lastScreen = lastScreen;
    }

    @Override
    public void init() {
        this.layout.spacing(15).defaultCellSetting().alignHorizontallyCenter();
        this.textWidget = new MultiLineTextWidget(MESSAGE, this.font).setCentered(true);
        this.layout.addChild(this.textWidget);
        LinearLayout buttonLayout = this.layout.addChild(LinearLayout.horizontal().spacing(8));
        MutableComponent privacyInfo = Component.translatable("mco.account.privacy.info.button");
        buttonLayout.addChild(Button.builder(privacyInfo, ConfirmLinkScreen.confirmLink((Screen)this, CommonLinks.GDPR)).build());
        buttonLayout.addChild(Button.builder(CommonComponents.GUI_BACK, button -> this.onClose()).build());
        RealmsParentalConsentScreen realmsParentalConsentScreen = this;
        this.layout.visitWidgets(x$0 -> realmsParentalConsentScreen.addRenderableWidget(x$0));
        this.repositionElements();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }

    @Override
    protected void repositionElements() {
        if (this.textWidget != null) {
            this.textWidget.setMaxWidth(this.width - 15);
        }
        this.layout.arrangeElements();
        FrameLayout.centerInRectangle(this.layout, this.getRectangle());
    }

    @Override
    public Component getNarrationMessage() {
        return MESSAGE;
    }
}

