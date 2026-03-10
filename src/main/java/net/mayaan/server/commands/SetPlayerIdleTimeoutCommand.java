/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 */
package net.mayaan.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.network.chat.Component;

public class SetPlayerIdleTimeoutCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("setidletimeout").requires(Commands.hasPermission(Commands.LEVEL_ADMINS))).then(Commands.argument("minutes", IntegerArgumentType.integer((int)0)).executes(c -> SetPlayerIdleTimeoutCommand.setIdleTimeout((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger((CommandContext)c, (String)"minutes")))));
    }

    private static int setIdleTimeout(CommandSourceStack source, int time) {
        source.getServer().setPlayerIdleTimeout(time);
        if (time > 0) {
            source.sendSuccess(() -> Component.translatable("commands.setidletimeout.success", time), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.setidletimeout.success.disabled"), true);
        }
        return time;
    }
}

