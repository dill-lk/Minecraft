/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.parsing.packrat;

import java.util.stream.Stream;
import net.mayaan.util.parsing.packrat.ParseState;

public interface SuggestionSupplier<S> {
    public Stream<String> possibleValues(ParseState<S> var1);

    public static <S> SuggestionSupplier<S> empty() {
        return state -> Stream.empty();
    }
}

