/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 */
package net.mayaan.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.Collection;
import java.util.Collections;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.commands.arguments.EntityArgument;
import net.mayaan.commands.arguments.coordinates.BlockPosArgument;
import net.mayaan.commands.arguments.coordinates.Coordinates;
import net.mayaan.commands.arguments.coordinates.RotationArgument;
import net.mayaan.commands.arguments.coordinates.WorldCoordinates;
import net.mayaan.core.BlockPos;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.util.Mth;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.storage.LevelData;
import net.mayaan.world.phys.Vec2;

public class SetSpawnCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spawnpoint").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).executes(c -> SetSpawnCommand.setSpawn((CommandSourceStack)c.getSource(), Collections.singleton(((CommandSourceStack)c.getSource()).getPlayerOrException()), BlockPos.containing(((CommandSourceStack)c.getSource()).getPosition()), WorldCoordinates.ZERO_ROTATION))).then(((RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.players()).executes(c -> SetSpawnCommand.setSpawn((CommandSourceStack)c.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)c, "targets"), BlockPos.containing(((CommandSourceStack)c.getSource()).getPosition()), WorldCoordinates.ZERO_ROTATION))).then(((RequiredArgumentBuilder)Commands.argument("pos", BlockPosArgument.blockPos()).executes(c -> SetSpawnCommand.setSpawn((CommandSourceStack)c.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)c, "targets"), BlockPosArgument.getSpawnablePos((CommandContext<CommandSourceStack>)c, "pos"), WorldCoordinates.ZERO_ROTATION))).then(Commands.argument("rotation", RotationArgument.rotation()).executes(c -> SetSpawnCommand.setSpawn((CommandSourceStack)c.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)c, "targets"), BlockPosArgument.getSpawnablePos((CommandContext<CommandSourceStack>)c, "pos"), RotationArgument.getRotation((CommandContext<CommandSourceStack>)c, "rotation")))))));
    }

    private static int setSpawn(CommandSourceStack source, Collection<ServerPlayer> targets, BlockPos pos, Coordinates rotation) {
        ResourceKey<Level> dimension = source.getLevel().dimension();
        Vec2 rotationVector = rotation.getRotation(source);
        float yaw = Mth.wrapDegrees(rotationVector.y);
        float pitch = Mth.clamp(rotationVector.x, -90.0f, 90.0f);
        for (ServerPlayer target : targets) {
            target.setRespawnPosition(new ServerPlayer.RespawnConfig(LevelData.RespawnData.of(dimension, pos, yaw, pitch), true), false);
        }
        String dimensionName = dimension.identifier().toString();
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.spawnpoint.success.single", pos.getX(), pos.getY(), pos.getZ(), Float.valueOf(yaw), Float.valueOf(pitch), dimensionName, ((ServerPlayer)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.spawnpoint.success.multiple", pos.getX(), pos.getY(), pos.getZ(), Float.valueOf(yaw), Float.valueOf(pitch), dimensionName, targets.size()), true);
        }
        return targets.size();
    }
}

