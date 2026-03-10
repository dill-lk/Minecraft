/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.parsing.packrat;

import net.mayaan.util.parsing.packrat.Atom;
import net.mayaan.util.parsing.packrat.Rule;

public interface NamedRule<S, T> {
    public Atom<T> name();

    public Rule<S, T> value();
}

