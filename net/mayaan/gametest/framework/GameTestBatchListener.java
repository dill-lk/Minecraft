/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest.framework;

import net.mayaan.gametest.framework.GameTestBatch;

public interface GameTestBatchListener {
    public void testBatchStarting(GameTestBatch var1);

    public void testBatchFinished(GameTestBatch var1);
}

