/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.brigadier.ImmutableStringReader
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.DynamicCommandExceptionType
 *  com.mojang.brigadier.suggestion.Suggestions
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 */
package net.mayaan.commands.arguments;

import com.google.common.collect.Lists;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.SharedSuggestionProvider;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.network.chat.Component;
import net.mayaan.stats.Stat;
import net.mayaan.stats.StatType;
import net.mayaan.world.scores.criteria.ObjectiveCriteria;

public class ObjectiveCriteriaArgument
implements ArgumentType<ObjectiveCriteria> {
    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo.bar.baz", "minecraft:foo");
    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(value -> Component.translatableEscape("argument.criteria.invalid", value));

    private ObjectiveCriteriaArgument() {
    }

    public static ObjectiveCriteriaArgument criteria() {
        return new ObjectiveCriteriaArgument();
    }

    public static ObjectiveCriteria getCriteria(CommandContext<CommandSourceStack> context, String name) {
        return (ObjectiveCriteria)context.getArgument(name, ObjectiveCriteria.class);
    }

    public ObjectiveCriteria parse(StringReader reader) throws CommandSyntaxException {
        int start = reader.getCursor();
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        String id = reader.getString().substring(start, reader.getCursor());
        return ObjectiveCriteria.byName(id).orElseThrow(() -> {
            reader.setCursor(start);
            return ERROR_INVALID_VALUE.createWithContext((ImmutableStringReader)reader, (Object)id);
        });
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        ArrayList ids = Lists.newArrayList(ObjectiveCriteria.getCustomCriteriaNames());
        for (StatType statType : BuiltInRegistries.STAT_TYPE) {
            for (Object value : statType.getRegistry()) {
                String name = this.getName(statType, value);
                ids.add(name);
            }
        }
        return SharedSuggestionProvider.suggest(ids, builder);
    }

    public <T> String getName(StatType<T> type, Object value) {
        return Stat.buildName(type, value);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}

