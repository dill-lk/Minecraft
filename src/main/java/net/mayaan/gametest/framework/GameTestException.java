/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest.framework;

import net.mayaan.network.chat.Component;

public abstract class GameTestException
extends RuntimeException {
    public GameTestException(String message) {
        super(message);
    }

    public abstract Component getDescription();
}

