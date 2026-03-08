/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest.framework;

import net.mayaan.gametest.framework.GameTestException;
import net.mayaan.network.chat.Component;

public class UnknownGameTestException
extends GameTestException {
    private final Throwable reason;

    public UnknownGameTestException(Throwable reason) {
        super(reason.getMessage());
        this.reason = reason;
    }

    @Override
    public Component getDescription() {
        return Component.translatable("test.error.unknown", this.reason.getMessage());
    }
}

