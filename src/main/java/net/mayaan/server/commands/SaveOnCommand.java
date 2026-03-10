/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 */
package net.mayaan.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.network.chat.Component;

public class SaveOnCommand {
    private static final SimpleCommandExceptionType ERROR_ALREADY_ON = new SimpleCommandExceptionType((Message)Component.translatable("commands.save.alreadyOn"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("save-on").requires(Commands.hasPermission(Commands.LEVEL_OWNERS))).executes(c -> {
            CommandSourceStack source = (CommandSourceStack)c.getSource();
            boolean success = source.getServer().setAutoSave(true);
            if (!success) {
                throw ERROR_ALREADY_ON.create();
            }
            source.sendSuccess(() -> Component.translatable("commands.save.enabled"), true);
            return 1;
        }));
    }
}

