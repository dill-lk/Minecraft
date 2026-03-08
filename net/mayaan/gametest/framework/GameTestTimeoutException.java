/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest.framework;

import net.mayaan.gametest.framework.GameTestException;
import net.mayaan.network.chat.Component;

public class GameTestTimeoutException
extends GameTestException {
    protected final Component message;

    public GameTestTimeoutException(Component message) {
        super(message.getString());
        this.message = message;
    }

    @Override
    public Component getDescription() {
        return this.message;
    }
}

