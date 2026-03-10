/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 */
package net.mayaan.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.commands.arguments.MessageArgument;
import net.mayaan.network.chat.ChatType;
import net.mayaan.network.chat.PlayerChatMessage;
import net.mayaan.server.players.PlayerList;

public class SayCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("say").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(Commands.argument("message", MessageArgument.message()).executes(c -> {
            MessageArgument.resolveChatMessage((CommandContext<CommandSourceStack>)c, "message", message -> {
                CommandSourceStack source = (CommandSourceStack)c.getSource();
                PlayerList playerList = source.getServer().getPlayerList();
                playerList.broadcastChatMessage((PlayerChatMessage)message, source, ChatType.bind(ChatType.SAY_COMMAND, source));
            });
            return 1;
        })));
    }
}

