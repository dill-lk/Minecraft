/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens;

import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.screens.ChatScreen;
import net.mayaan.client.multiplayer.ClientPacketListener;
import net.mayaan.network.chat.Component;
import net.mayaan.network.protocol.game.ServerboundPlayerCommandPacket;

public class InBedChatScreen
extends ChatScreen {
    private Button leaveBedButton;

    public InBedChatScreen(String initial, boolean isDraft) {
        super(initial, isDraft, false);
    }

    @Override
    protected void init() {
        super.init();
        this.leaveBedButton = Button.builder(Component.translatable("multiplayer.stopSleeping"), button -> this.sendWakeUp()).bounds(this.width / 2 - 100, this.height - 40, 200, 20).build();
        this.addRenderableWidget(this.leaveBedButton);
    }

    @Override
    public void onClose() {
        this.sendWakeUp();
    }

    private void sendWakeUp() {
        ClientPacketListener connection = this.minecraft.player.connection;
        connection.send(new ServerboundPlayerCommandPacket(this.minecraft.player, ServerboundPlayerCommandPacket.Action.STOP_SLEEPING));
    }

    public void onPlayerWokeUp() {
        String text = this.input.getValue();
        if (this.isDraft || text.isEmpty()) {
            this.exitReason = ChatScreen.ExitReason.INTERRUPTED;
            this.minecraft.setScreen(null);
        } else {
            this.exitReason = ChatScreen.ExitReason.DONE;
            this.minecraft.setScreen(new ChatScreen(text, false));
        }
    }
}

