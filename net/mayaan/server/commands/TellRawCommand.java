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
import net.mayaan.commands.CommandBuildContext;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.commands.arguments.ComponentArgument;
import net.mayaan.commands.arguments.EntityArgument;
import net.mayaan.server.level.ServerPlayer;

public class TellRawCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tellraw").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("message", ComponentArgument.textComponent(context)).executes(c -> {
            int result = 0;
            for (ServerPlayer player : EntityArgument.getPlayers((CommandContext<CommandSourceStack>)c, "targets")) {
                player.sendSystemMessage(ComponentArgument.getResolvedComponent((CommandContext<CommandSourceStack>)c, "message", player));
                ++result;
            }
            return result;
        }))));
    }
}

