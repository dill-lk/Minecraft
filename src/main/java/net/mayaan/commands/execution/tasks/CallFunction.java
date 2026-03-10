/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.commands.execution.tasks;

import java.util.List;
import net.mayaan.commands.CommandResultCallback;
import net.mayaan.commands.ExecutionCommandSource;
import net.mayaan.commands.execution.CommandQueueEntry;
import net.mayaan.commands.execution.ExecutionContext;
import net.mayaan.commands.execution.Frame;
import net.mayaan.commands.execution.TraceCallbacks;
import net.mayaan.commands.execution.UnboundEntryAction;
import net.mayaan.commands.execution.tasks.ContinuationTask;
import net.mayaan.commands.functions.InstantiatedFunction;

public class CallFunction<T extends ExecutionCommandSource<T>>
implements UnboundEntryAction<T> {
    private final InstantiatedFunction<T> function;
    private final CommandResultCallback resultCallback;
    private final boolean returnParentFrame;

    public CallFunction(InstantiatedFunction<T> function, CommandResultCallback resultCallback, boolean returnParentFrame) {
        this.function = function;
        this.resultCallback = resultCallback;
        this.returnParentFrame = returnParentFrame;
    }

    @Override
    public void execute(T sender, ExecutionContext<T> context, Frame frame) {
        context.incrementCost();
        List<UnboundEntryAction<T>> contents = this.function.entries();
        TraceCallbacks tracer = context.tracer();
        if (tracer != null) {
            tracer.onCall(frame.depth(), this.function.id(), this.function.entries().size());
        }
        int newDepth = frame.depth() + 1;
        Frame.FrameControl frameControl = this.returnParentFrame ? frame.frameControl() : context.frameControlForDepth(newDepth);
        Frame newFrame = new Frame(newDepth, this.resultCallback, frameControl);
        ContinuationTask.schedule(context, newFrame, contents, (frame1, entryAction) -> new CommandQueueEntry<ExecutionCommandSource>(frame1, entryAction.bind(sender)));
    }
}

