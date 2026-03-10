/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 */
package net.mayaan.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.commands.arguments.coordinates.BlockPosArgument;
import net.mayaan.core.BlockPos;
import net.mayaan.network.chat.Component;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.ai.navigation.GroundPathNavigation;
import net.mayaan.world.entity.ai.navigation.PathNavigation;
import net.mayaan.world.level.pathfinder.Path;

public class DebugPathCommand {
    private static final SimpleCommandExceptionType ERROR_NOT_MOB = new SimpleCommandExceptionType((Message)Component.literal("Source is not a mob"));
    private static final SimpleCommandExceptionType ERROR_NO_PATH = new SimpleCommandExceptionType((Message)Component.literal("Path not found"));
    private static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)Component.literal("Target not reached"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("debugpath").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(Commands.argument("to", BlockPosArgument.blockPos()).executes(c -> DebugPathCommand.fillBlocks((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)c, "to")))));
    }

    private static int fillBlocks(CommandSourceStack source, BlockPos target) throws CommandSyntaxException {
        Entity entity = source.getEntity();
        if (!(entity instanceof Mob)) {
            throw ERROR_NOT_MOB.create();
        }
        Mob mob = (Mob)entity;
        GroundPathNavigation pathNavigation = new GroundPathNavigation(mob, source.getLevel());
        Path path = ((PathNavigation)pathNavigation).createPath(target, 0);
        if (path == null) {
            throw ERROR_NO_PATH.create();
        }
        if (!path.canReach()) {
            throw ERROR_NOT_COMPLETE.create();
        }
        source.sendSuccess(() -> Component.literal("Made path"), true);
        return 1;
    }
}

