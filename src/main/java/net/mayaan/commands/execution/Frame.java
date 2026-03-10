/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.commands.execution;

import net.mayaan.commands.CommandResultCallback;

public record Frame(int depth, CommandResultCallback returnValueConsumer, FrameControl frameControl) {
    public void returnSuccess(int value) {
        this.returnValueConsumer.onSuccess(value);
    }

    public void returnFailure() {
        this.returnValueConsumer.onFailure();
    }

    public void discard() {
        this.frameControl.discard();
    }

    @FunctionalInterface
    public static interface FrameControl {
        public void discard();
    }
}

