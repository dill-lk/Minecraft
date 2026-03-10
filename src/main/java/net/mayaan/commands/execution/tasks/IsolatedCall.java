/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.commands.execution.tasks;

import java.util.function.Consumer;
import net.mayaan.commands.CommandResultCallback;
import net.mayaan.commands.ExecutionCommandSource;
import net.mayaan.commands.execution.EntryAction;
import net.mayaan.commands.execution.ExecutionContext;
import net.mayaan.commands.execution.ExecutionControl;
import net.mayaan.commands.execution.Frame;

public class IsolatedCall<T extends ExecutionCommandSource<T>>
implements EntryAction<T> {
    private final Consumer<ExecutionControl<T>> taskProducer;
    private final CommandResultCallback output;

    public IsolatedCall(Consumer<ExecutionControl<T>> taskOutput, CommandResultCallback output) {
        this.taskProducer = taskOutput;
        this.output = output;
    }

    @Override
    public void execute(ExecutionContext<T> context, Frame frame) {
        int newFrameDepth = frame.depth() + 1;
        Frame newFrame = new Frame(newFrameDepth, this.output, context.frameControlForDepth(newFrameDepth));
        this.taskProducer.accept(ExecutionControl.create(context, newFrame));
    }
}

