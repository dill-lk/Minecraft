/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest.framework;

import net.mayaan.gametest.framework.GameTestInfo;

class ExhaustedAttemptsException
extends Throwable {
    public ExhaustedAttemptsException(int attempts, int successes, GameTestInfo testInfo) {
        super("Not enough successes: " + successes + " out of " + attempts + " attempts. Required successes: " + testInfo.requiredSuccesses() + ". max attempts: " + testInfo.maxAttempts() + ".", testInfo.getError());
    }
}

