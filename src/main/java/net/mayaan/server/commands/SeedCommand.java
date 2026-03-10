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
import net.mayaan.network.chat.ComponentUtils;
import net.mayaan.network.chat.MutableComponent;

public class SeedCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean checkPermissions) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("seed").requires(Commands.hasPermission(checkPermissions ? Commands.LEVEL_GAMEMASTERS : Commands.LEVEL_ALL))).executes(c -> {
            long seed = ((CommandSourceStack)c.getSource()).getLevel().getSeed();
            MutableComponent seedText = ComponentUtils.copyOnClickText(String.valueOf(seed));
            ((CommandSourceStack)c.getSource()).sendSuccess(() -> Component.translatable("commands.seed.success", seedText), false);
            return (int)seed;
        }));
    }
}

