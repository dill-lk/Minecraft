/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 *  com.mojang.brigadier.suggestion.Suggestions
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 */
package net.mayaan.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.commands.SharedSuggestionProvider;
import net.mayaan.commands.arguments.ObjectiveArgument;
import net.mayaan.network.chat.Component;
import net.mayaan.server.ServerScoreboard;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.scores.Objective;
import net.mayaan.world.scores.ReadOnlyScoreInfo;
import net.mayaan.world.scores.ScoreAccess;
import net.mayaan.world.scores.ScoreHolder;
import net.mayaan.world.scores.Scoreboard;
import net.mayaan.world.scores.criteria.ObjectiveCriteria;

public class TriggerCommand {
    private static final SimpleCommandExceptionType ERROR_NOT_PRIMED = new SimpleCommandExceptionType((Message)Component.translatable("commands.trigger.failed.unprimed"));
    private static final SimpleCommandExceptionType ERROR_INVALID_OBJECTIVE = new SimpleCommandExceptionType((Message)Component.translatable("commands.trigger.failed.invalid"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.literal("trigger").then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("objective", ObjectiveArgument.objective()).suggests((c, p) -> TriggerCommand.suggestObjectives((CommandSourceStack)c.getSource(), p)).executes(c -> TriggerCommand.simpleTrigger((CommandSourceStack)c.getSource(), ((CommandSourceStack)c.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective((CommandContext<CommandSourceStack>)c, "objective")))).then(Commands.literal("add").then(Commands.argument("value", IntegerArgumentType.integer()).executes(c -> TriggerCommand.addValue((CommandSourceStack)c.getSource(), ((CommandSourceStack)c.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective((CommandContext<CommandSourceStack>)c, "objective"), IntegerArgumentType.getInteger((CommandContext)c, (String)"value")))))).then(Commands.literal("set").then(Commands.argument("value", IntegerArgumentType.integer()).executes(c -> TriggerCommand.setValue((CommandSourceStack)c.getSource(), ((CommandSourceStack)c.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective((CommandContext<CommandSourceStack>)c, "objective"), IntegerArgumentType.getInteger((CommandContext)c, (String)"value")))))));
    }

    public static CompletableFuture<Suggestions> suggestObjectives(CommandSourceStack source, SuggestionsBuilder builder) {
        Entity entity = source.getEntity();
        ArrayList result = Lists.newArrayList();
        if (entity != null) {
            ServerScoreboard scoreboard = source.getServer().getScoreboard();
            for (Objective objective : scoreboard.getObjectives()) {
                ReadOnlyScoreInfo scoreInfo;
                if (objective.getCriteria() != ObjectiveCriteria.TRIGGER || (scoreInfo = scoreboard.getPlayerScoreInfo(entity, objective)) == null || scoreInfo.isLocked()) continue;
                result.add(objective.getName());
            }
        }
        return SharedSuggestionProvider.suggest(result, builder);
    }

    private static int addValue(CommandSourceStack source, ServerPlayer player, Objective objective, int amount) throws CommandSyntaxException {
        ScoreAccess score = TriggerCommand.getScore(source.getServer().getScoreboard(), player, objective);
        int newValue = score.add(amount);
        source.sendSuccess(() -> Component.translatable("commands.trigger.add.success", objective.getFormattedDisplayName(), amount), true);
        return newValue;
    }

    private static int setValue(CommandSourceStack source, ServerPlayer player, Objective objective, int amount) throws CommandSyntaxException {
        ScoreAccess score = TriggerCommand.getScore(source.getServer().getScoreboard(), player, objective);
        score.set(amount);
        source.sendSuccess(() -> Component.translatable("commands.trigger.set.success", objective.getFormattedDisplayName(), amount), true);
        return amount;
    }

    private static int simpleTrigger(CommandSourceStack source, ServerPlayer player, Objective objective) throws CommandSyntaxException {
        ScoreAccess score = TriggerCommand.getScore(source.getServer().getScoreboard(), player, objective);
        int newValue = score.add(1);
        source.sendSuccess(() -> Component.translatable("commands.trigger.simple.success", objective.getFormattedDisplayName()), true);
        return newValue;
    }

    private static ScoreAccess getScore(Scoreboard scoreboard, ScoreHolder scoreHolder, Objective objective) throws CommandSyntaxException {
        if (objective.getCriteria() != ObjectiveCriteria.TRIGGER) {
            throw ERROR_INVALID_OBJECTIVE.create();
        }
        ReadOnlyScoreInfo scoreInfo = scoreboard.getPlayerScoreInfo(scoreHolder, objective);
        if (scoreInfo == null || scoreInfo.isLocked()) {
            throw ERROR_NOT_PRIMED.create();
        }
        ScoreAccess score = scoreboard.getOrCreatePlayerScore(scoreHolder, objective);
        score.lock();
        return score;
    }
}

