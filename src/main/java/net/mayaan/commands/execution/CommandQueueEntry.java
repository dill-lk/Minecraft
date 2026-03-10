/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.commands.execution;

import net.mayaan.commands.execution.EntryAction;
import net.mayaan.commands.execution.ExecutionContext;
import net.mayaan.commands.execution.Frame;

public record CommandQueueEntry<T>(Frame frame, EntryAction<T> action) {
    public void execute(ExecutionContext<T> context) {
        this.action.execute(context, this.frame);
    }
}

