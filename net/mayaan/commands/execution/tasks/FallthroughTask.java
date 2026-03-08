/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.commands.execution.tasks;

import net.mayaan.commands.ExecutionCommandSource;
import net.mayaan.commands.execution.EntryAction;
import net.mayaan.commands.execution.ExecutionContext;
import net.mayaan.commands.execution.Frame;

public class FallthroughTask<T extends ExecutionCommandSource<T>>
implements EntryAction<T> {
    private static final FallthroughTask<? extends ExecutionCommandSource<?>> INSTANCE = new FallthroughTask();

    public static <T extends ExecutionCommandSource<T>> EntryAction<T> instance() {
        return INSTANCE;
    }

    @Override
    public void execute(ExecutionContext<T> context, Frame frame) {
        frame.returnFailure();
        frame.discard();
    }
}

