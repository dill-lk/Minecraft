/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.server.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.HashSet;
import java.util.UUID;
import net.mayaan.commands.CommandBuildContext;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.commands.SharedSuggestionProvider;
import net.mayaan.commands.arguments.EntityArgument;
import net.mayaan.commands.arguments.ResourceOrIdArgument;
import net.mayaan.commands.arguments.UuidArgument;
import net.mayaan.core.Holder;
import net.mayaan.network.Connection;
import net.mayaan.network.PacketListener;
import net.mayaan.network.chat.Component;
import net.mayaan.network.protocol.common.ClientboundShowDialogPacket;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.dialog.Dialog;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.server.network.ServerConfigurationPacketListenerImpl;
import org.jspecify.annotations.Nullable;

public class DebugConfigCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("debugconfig").requires(Commands.hasPermission(Commands.LEVEL_ADMINS))).then(Commands.literal("config").then(Commands.argument("target", EntityArgument.player()).executes(c -> DebugConfigCommand.config((CommandSourceStack)c.getSource(), EntityArgument.getPlayer((CommandContext<CommandSourceStack>)c, "target")))))).then(Commands.literal("unconfig").then(Commands.argument("target", UuidArgument.uuid()).suggests((c, p) -> SharedSuggestionProvider.suggest(DebugConfigCommand.getUuidsInConfig(((CommandSourceStack)c.getSource()).getServer()), p)).executes(c -> DebugConfigCommand.unconfig((CommandSourceStack)c.getSource(), UuidArgument.getUuid((CommandContext<CommandSourceStack>)c, "target")))))).then(Commands.literal("dialog").then(Commands.argument("target", UuidArgument.uuid()).suggests((c, p) -> SharedSuggestionProvider.suggest(DebugConfigCommand.getUuidsInConfig(((CommandSourceStack)c.getSource()).getServer()), p)).then(Commands.argument("dialog", ResourceOrIdArgument.dialog(context)).executes(c -> DebugConfigCommand.showDialog((CommandSourceStack)c.getSource(), UuidArgument.getUuid((CommandContext<CommandSourceStack>)c, "target"), ResourceOrIdArgument.getDialog((CommandContext<CommandSourceStack>)c, "dialog")))))));
    }

    private static Iterable<String> getUuidsInConfig(MayaanServer server) {
        HashSet<String> result = new HashSet<String>();
        for (Connection connection : server.getConnection().getConnections()) {
            PacketListener packetListener = connection.getPacketListener();
            if (!(packetListener instanceof ServerConfigurationPacketListenerImpl)) continue;
            ServerConfigurationPacketListenerImpl configListener = (ServerConfigurationPacketListenerImpl)packetListener;
            result.add(configListener.getOwner().id().toString());
        }
        return result;
    }

    private static int config(CommandSourceStack source, ServerPlayer target) {
        GameProfile gameProfile = target.getGameProfile();
        target.connection.switchToConfig();
        source.sendSuccess(() -> Component.literal("Switched player " + gameProfile.name() + "(" + String.valueOf(gameProfile.id()) + ") to config mode"), false);
        return 1;
    }

    private static @Nullable ServerConfigurationPacketListenerImpl findConfigPlayer(MayaanServer server, UUID target) {
        for (Connection connection : server.getConnection().getConnections()) {
            ServerConfigurationPacketListenerImpl configListener;
            PacketListener packetListener = connection.getPacketListener();
            if (!(packetListener instanceof ServerConfigurationPacketListenerImpl) || !(configListener = (ServerConfigurationPacketListenerImpl)packetListener).getOwner().id().equals(target)) continue;
            return configListener;
        }
        return null;
    }

    private static int unconfig(CommandSourceStack source, UUID target) {
        ServerConfigurationPacketListenerImpl listener = DebugConfigCommand.findConfigPlayer(source.getServer(), target);
        if (listener != null) {
            listener.returnToWorld();
            return 1;
        }
        source.sendFailure(Component.literal("Can't find player to unconfig"));
        return 0;
    }

    private static int showDialog(CommandSourceStack source, UUID target, Holder<Dialog> dialog) {
        ServerConfigurationPacketListenerImpl listener = DebugConfigCommand.findConfigPlayer(source.getServer(), target);
        if (listener != null) {
            listener.send(new ClientboundShowDialogPacket(dialog));
            return 1;
        }
        source.sendFailure(Component.literal("Can't find player to talk to"));
        return 0;
    }
}

