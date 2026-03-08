/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.commands.execution;

import net.mayaan.commands.execution.ExecutionContext;
import net.mayaan.commands.execution.Frame;

@FunctionalInterface
public interface EntryAction<T> {
    public void execute(ExecutionContext<T> var1, Frame var2);
}

