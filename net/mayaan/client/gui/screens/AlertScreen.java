/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens;

import net.mayaan.client.gui.ActiveTextCollector;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.TextAlignment;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.MultiLineLabel;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.util.Mth;

public class AlertScreen
extends Screen {
    private static final int LABEL_Y = 90;
    private final Component messageText;
    private MultiLineLabel message = MultiLineLabel.EMPTY;
    private final Runnable callback;
    private final Component okButton;
    private final boolean shouldCloseOnEsc;

    public AlertScreen(Runnable callback, Component title, Component messageText) {
        this(callback, title, messageText, CommonComponents.GUI_BACK, true);
    }

    public AlertScreen(Runnable callback, Component title, Component messageText, Component okButton, boolean shouldCloseOnEsc) {
        super(title);
        this.callback = callback;
        this.messageText = messageText;
        this.okButton = okButton;
        this.shouldCloseOnEsc = shouldCloseOnEsc;
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(super.getNarrationMessage(), this.messageText);
    }

    @Override
    protected void init() {
        super.init();
        this.message = MultiLineLabel.create(this.font, this.messageText, this.width - 50);
        int textHeight = this.message.getLineCount() * this.font.lineHeight;
        int buttonY = Mth.clamp(90 + textHeight + 12, this.height / 6 + 96, this.height - 24);
        int buttonWidth = 150;
        this.addRenderableWidget(Button.builder(this.okButton, button -> this.callback.run()).bounds((this.width - 150) / 2, buttonY, 150, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        super.render(graphics, mouseX, mouseY, a);
        ActiveTextCollector textRenderer = graphics.textRenderer();
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 70, -1);
        this.message.visitLines(TextAlignment.CENTER, this.width / 2, 90, this.font.lineHeight, textRenderer);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return this.shouldCloseOnEsc;
    }
}

