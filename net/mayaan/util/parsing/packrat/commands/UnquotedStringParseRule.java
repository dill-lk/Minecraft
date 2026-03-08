/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.util.parsing.packrat.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mayaan.util.parsing.packrat.DelayedException;
import net.mayaan.util.parsing.packrat.ParseState;
import net.mayaan.util.parsing.packrat.Rule;
import org.jspecify.annotations.Nullable;

public class UnquotedStringParseRule
implements Rule<StringReader, String> {
    private final int minSize;
    private final DelayedException<CommandSyntaxException> error;

    public UnquotedStringParseRule(int minSize, DelayedException<CommandSyntaxException> error) {
        this.minSize = minSize;
        this.error = error;
    }

    @Override
    public @Nullable String parse(ParseState<StringReader> state) {
        state.input().skipWhitespace();
        int cursor = state.mark();
        String value = state.input().readUnquotedString();
        if (value.length() < this.minSize) {
            state.errorCollector().store(cursor, this.error);
            return null;
        }
        return value;
    }
}

