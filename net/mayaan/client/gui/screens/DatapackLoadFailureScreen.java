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

public class DatapackLoadFailureScreen
extends Screen {
    private MultiLineLabel message = MultiLineLabel.EMPTY;
    private final Runnable cancelCallback;
    private final Runnable safeModeCallback;

    public DatapackLoadFailureScreen(Runnable cancelCallback, Runnable safeModeCallback) {
        super(Component.translatable("datapackFailure.title"));
        this.cancelCallback = cancelCallback;
        this.safeModeCallback = safeModeCallback;
    }

    @Override
    protected void init() {
        super.init();
        this.message = MultiLineLabel.create(this.font, this.getTitle(), this.width - 50);
        this.addRenderableWidget(Button.builder(Component.translatable("datapackFailure.safeMode"), button -> this.safeModeCallback.run()).bounds(this.width / 2 - 155, this.height / 6 + 96, 150, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, button -> this.cancelCallback.run()).bounds(this.width / 2 - 155 + 160, this.height / 6 + 96, 150, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        super.render(graphics, mouseX, mouseY, a);
        ActiveTextCollector textRenderer = graphics.textRenderer();
        this.message.visitLines(TextAlignment.CENTER, this.width / 2, 70, this.font.lineHeight, textRenderer);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}

