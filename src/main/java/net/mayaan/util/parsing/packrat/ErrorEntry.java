/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.parsing.packrat;

import net.mayaan.util.parsing.packrat.SuggestionSupplier;

public record ErrorEntry<S>(int cursor, SuggestionSupplier<S> suggestions, Object reason) {
}

