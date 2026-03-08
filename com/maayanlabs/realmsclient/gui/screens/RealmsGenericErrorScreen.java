/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.realmsclient.gui.screens;

import com.maayanlabs.realmsclient.client.RealmsError;
import com.maayanlabs.realmsclient.exception.RealmsServiceException;
import net.mayaan.client.gui.ActiveTextCollector;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.TextAlignment;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.MultiLineLabel;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentUtils;
import net.mayaan.network.chat.Style;
import net.mayaan.realms.RealmsScreen;

public class RealmsGenericErrorScreen
extends RealmsScreen {
    private static final Component GENERIC_TITLE = Component.translatable("mco.errorMessage.generic");
    private final Screen nextScreen;
    private final Component detail;
    private MultiLineLabel splitDetail = MultiLineLabel.EMPTY;

    public RealmsGenericErrorScreen(RealmsServiceException realmsServiceException, Screen nextScreen) {
        this(ErrorMessage.forServiceError(realmsServiceException), nextScreen);
    }

    public RealmsGenericErrorScreen(Component message, Screen nextScreen) {
        this(new ErrorMessage(GENERIC_TITLE, message), nextScreen);
    }

    public RealmsGenericErrorScreen(Component title, Component message, Screen nextScreen) {
        this(new ErrorMessage(title, message), nextScreen);
    }

    private RealmsGenericErrorScreen(ErrorMessage message, Screen nextScreen) {
        super(message.title);
        this.nextScreen = nextScreen;
        this.detail = ComponentUtils.mergeStyles(message.detail, Style.EMPTY.withColor(-2142128));
    }

    @Override
    public void init() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_OK, button -> this.onClose()).bounds(this.width / 2 - 100, this.height - 52, 200, 20).build());
        this.splitDetail = MultiLineLabel.create(this.font, this.detail, this.width * 3 / 4);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.nextScreen);
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(super.getNarrationMessage(), this.detail);
    }

    @Override
    public void render(GuiGraphics graphics, int xm, int ym, float a) {
        super.render(graphics, xm, ym, a);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 80, -1);
        ActiveTextCollector textRenderer = graphics.textRenderer();
        this.splitDetail.visitLines(TextAlignment.CENTER, this.width / 2, 100, this.minecraft.font.lineHeight, textRenderer);
    }

    private record ErrorMessage(Component title, Component detail) {
        private static ErrorMessage forServiceError(RealmsServiceException realmsServiceException) {
            RealmsError errorDetails = realmsServiceException.realmsError;
            return new ErrorMessage(Component.translatable("mco.errorMessage.realmsService.realmsError", errorDetails.errorCode()), errorDetails.errorMessage());
        }
    }
}

