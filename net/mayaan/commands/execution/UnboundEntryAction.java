/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.commands.execution;

import net.mayaan.commands.execution.EntryAction;
import net.mayaan.commands.execution.ExecutionContext;
import net.mayaan.commands.execution.Frame;

@FunctionalInterface
public interface UnboundEntryAction<T> {
    public void execute(T var1, ExecutionContext<T> var2, Frame var3);

    default public EntryAction<T> bind(T sender) {
        return (context, frame) -> this.execute(sender, context, frame);
    }
}

