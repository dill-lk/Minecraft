/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.booleans.BooleanConsumer
 */
package net.mayaan.client.gui.screens;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.EditBox;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.input.KeyEvent;
import net.mayaan.client.multiplayer.ServerData;
import net.mayaan.client.multiplayer.resolver.ServerAddress;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;

public class DirectJoinServerScreen
extends Screen {
    private static final Component ENTER_IP_LABEL = Component.translatable("manageServer.enterIp");
    private Button selectButton;
    private final ServerData serverData;
    private EditBox ipEdit;
    private final BooleanConsumer callback;
    private final Screen lastScreen;

    public DirectJoinServerScreen(Screen lastScreen, BooleanConsumer callback, ServerData serverData) {
        super(Component.translatable("selectServer.direct"));
        this.lastScreen = lastScreen;
        this.serverData = serverData;
        this.callback = callback;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.selectButton.active && this.getFocused() == this.ipEdit && event.isConfirmation()) {
            this.onSelect();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    protected void init() {
        this.ipEdit = new EditBox(this.font, this.width / 2 - 100, 116, 200, 20, ENTER_IP_LABEL);
        this.ipEdit.setMaxLength(128);
        this.ipEdit.setValue(this.minecraft.options.lastMpIp);
        this.ipEdit.setResponder(value -> this.updateSelectButtonStatus());
        this.addWidget(this.ipEdit);
        this.selectButton = this.addRenderableWidget(Button.builder(Component.translatable("selectServer.select"), button -> this.onSelect()).bounds(this.width / 2 - 100, this.height / 4 + 96 + 12, 200, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> this.callback.accept(false)).bounds(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20).build());
        this.updateSelectButtonStatus();
    }

    @Override
    protected void setInitialFocus() {
        this.setInitialFocus(this.ipEdit);
    }

    @Override
    public void resize(int width, int height) {
        String oldEdit = this.ipEdit.getValue();
        this.init(width, height);
        this.ipEdit.setValue(oldEdit);
    }

    private void onSelect() {
        this.serverData.ip = this.ipEdit.getValue();
        this.callback.accept(true);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }

    @Override
    public void removed() {
        this.minecraft.options.lastMpIp = this.ipEdit.getValue();
        this.minecraft.options.save();
    }

    private void updateSelectButtonStatus() {
        this.selectButton.active = ServerAddress.isValidAddress(this.ipEdit.getValue());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        super.render(graphics, mouseX, mouseY, a);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, -1);
        graphics.drawString(this.font, ENTER_IP_LABEL, this.width / 2 - 100 + 1, 100, -6250336);
        this.ipEdit.render(graphics, mouseX, mouseY, a);
    }
}

