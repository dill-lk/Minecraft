/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.chat;

import net.mayaan.network.chat.ChatType;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.PlayerChatMessage;
import net.mayaan.server.level.ServerPlayer;

public interface OutgoingChatMessage {
    public Component content();

    public void sendToPlayer(ServerPlayer var1, boolean var2, ChatType.Bound var3);

    public static OutgoingChatMessage create(PlayerChatMessage message) {
        if (message.isSystem()) {
            return new Disguised(message.decoratedContent());
        }
        return new Player(message);
    }

    public record Disguised(Component content) implements OutgoingChatMessage
    {
        @Override
        public void sendToPlayer(ServerPlayer player, boolean filtered, ChatType.Bound chatType) {
            player.connection.sendDisguisedChatMessage(this.content, chatType);
        }
    }

    public record Player(PlayerChatMessage message) implements OutgoingChatMessage
    {
        @Override
        public Component content() {
            return this.message.decoratedContent();
        }

        @Override
        public void sendToPlayer(ServerPlayer player, boolean filtered, ChatType.Bound chatType) {
            PlayerChatMessage filteredMessage = this.message.filter(filtered);
            if (!filteredMessage.isFullyFiltered()) {
                player.connection.sendPlayerChatMessage(filteredMessage, chatType);
            }
        }
    }
}

