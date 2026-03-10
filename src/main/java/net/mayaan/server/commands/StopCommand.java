/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 */
package net.mayaan.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.network.chat.Component;

public class StopCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("stop").requires(Commands.hasPermission(Commands.LEVEL_OWNERS))).executes(c -> {
            ((CommandSourceStack)c.getSource()).sendSuccess(() -> Component.translatable("commands.stop.stopping"), true);
            ((CommandSourceStack)c.getSource()).getServer().halt(false);
            return 1;
        }));
    }
}

