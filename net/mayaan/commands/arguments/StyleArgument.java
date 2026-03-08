/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.DynamicCommandExceptionType
 *  com.mojang.serialization.DynamicOps
 */
package net.mayaan.commands.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.serialization.DynamicOps;
import java.util.Collection;
import java.util.List;
import net.mayaan.commands.CommandBuildContext;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.core.HolderLookup;
import net.mayaan.nbt.NbtOps;
import net.mayaan.nbt.SnbtGrammar;
import net.mayaan.nbt.Tag;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.Style;
import net.mayaan.util.parsing.packrat.commands.CommandArgumentParser;
import net.mayaan.util.parsing.packrat.commands.ParserBasedArgument;

public class StyleArgument
extends ParserBasedArgument<Style> {
    private static final Collection<String> EXAMPLES = List.of("{bold: true}", "{color: 'red'}", "{}");
    public static final DynamicCommandExceptionType ERROR_INVALID_STYLE = new DynamicCommandExceptionType(message -> Component.translatableEscape("argument.style.invalid", message));
    private static final DynamicOps<Tag> OPS = NbtOps.INSTANCE;
    private static final CommandArgumentParser<Tag> TAG_PARSER = SnbtGrammar.createParser(OPS);

    private StyleArgument(HolderLookup.Provider registries) {
        super(TAG_PARSER.withCodec(registries.createSerializationContext(OPS), TAG_PARSER, Style.Serializer.CODEC, ERROR_INVALID_STYLE));
    }

    public static Style getStyle(CommandContext<CommandSourceStack> context, String name) {
        return (Style)context.getArgument(name, Style.class);
    }

    public static StyleArgument style(CommandBuildContext context) {
        return new StyleArgument(context);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}

