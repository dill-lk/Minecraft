/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.commands.functions;

import java.util.List;
import net.mayaan.commands.execution.UnboundEntryAction;
import net.mayaan.resources.Identifier;

public interface InstantiatedFunction<T> {
    public Identifier id();

    public List<UnboundEntryAction<T>> entries();
}

