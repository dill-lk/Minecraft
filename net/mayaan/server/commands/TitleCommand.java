/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 */
package net.mayaan.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import java.util.function.Function;
import net.mayaan.commands.CommandBuildContext;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.commands.arguments.ComponentArgument;
import net.mayaan.commands.arguments.EntityArgument;
import net.mayaan.commands.arguments.TimeArgument;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentUtils;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.game.ClientboundClearTitlesPacket;
import net.mayaan.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.mayaan.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.mayaan.network.protocol.game.ClientboundSetTitleTextPacket;
import net.mayaan.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.entity.Entity;

public class TitleCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("title").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.players()).then(Commands.literal("clear").executes(c -> TitleCommand.clearTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)c, "targets"))))).then(Commands.literal("reset").executes(c -> TitleCommand.resetTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)c, "targets"))))).then(Commands.literal("title").then(Commands.argument("title", ComponentArgument.textComponent(context)).executes(c -> TitleCommand.showTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)c, "targets"), ComponentArgument.getRawComponent((CommandContext<CommandSourceStack>)c, "title"), "title", ClientboundSetTitleTextPacket::new))))).then(Commands.literal("subtitle").then(Commands.argument("title", ComponentArgument.textComponent(context)).executes(c -> TitleCommand.showTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)c, "targets"), ComponentArgument.getRawComponent((CommandContext<CommandSourceStack>)c, "title"), "subtitle", ClientboundSetSubtitleTextPacket::new))))).then(Commands.literal("actionbar").then(Commands.argument("title", ComponentArgument.textComponent(context)).executes(c -> TitleCommand.showTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)c, "targets"), ComponentArgument.getRawComponent((CommandContext<CommandSourceStack>)c, "title"), "actionbar", ClientboundSetActionBarTextPacket::new))))).then(Commands.literal("times").then(Commands.argument("fadeIn", TimeArgument.time()).then(Commands.argument("stay", TimeArgument.time()).then(Commands.argument("fadeOut", TimeArgument.time()).executes(c -> TitleCommand.setTimes((CommandSourceStack)c.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)c, "targets"), IntegerArgumentType.getInteger((CommandContext)c, (String)"fadeIn"), IntegerArgumentType.getInteger((CommandContext)c, (String)"stay"), IntegerArgumentType.getInteger((CommandContext)c, (String)"fadeOut")))))))));
    }

    private static int clearTitle(CommandSourceStack source, Collection<ServerPlayer> targets) {
        ClientboundClearTitlesPacket packet = new ClientboundClearTitlesPacket(false);
        for (ServerPlayer player : targets) {
            player.connection.send(packet);
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.title.cleared.single", ((ServerPlayer)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.title.cleared.multiple", targets.size()), true);
        }
        return targets.size();
    }

    private static int resetTitle(CommandSourceStack source, Collection<ServerPlayer> targets) {
        ClientboundClearTitlesPacket packet = new ClientboundClearTitlesPacket(true);
        for (ServerPlayer player : targets) {
            player.connection.send(packet);
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.title.reset.single", ((ServerPlayer)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.title.reset.multiple", targets.size()), true);
        }
        return targets.size();
    }

    private static int showTitle(CommandSourceStack source, Collection<ServerPlayer> targets, Component title, String type, Function<Component, Packet<?>> factory) throws CommandSyntaxException {
        for (ServerPlayer player : targets) {
            player.connection.send(factory.apply(ComponentUtils.updateForEntity(source, title, (Entity)player, 0)));
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.title.show." + type + ".single", ((ServerPlayer)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.title.show." + type + ".multiple", targets.size()), true);
        }
        return targets.size();
    }

    private static int setTimes(CommandSourceStack source, Collection<ServerPlayer> targets, int fadeIn, int stay, int fadeOut) {
        ClientboundSetTitlesAnimationPacket packet = new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut);
        for (ServerPlayer player : targets) {
            player.connection.send(packet);
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.title.times.single", ((ServerPlayer)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.title.times.multiple", targets.size()), true);
        }
        return targets.size();
    }
}

