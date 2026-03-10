/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest.framework;

import net.mayaan.gametest.framework.GameTestInfo;
import net.mayaan.gametest.framework.GameTestRunner;

public interface GameTestListener {
    public void testStructureLoaded(GameTestInfo var1);

    public void testPassed(GameTestInfo var1, GameTestRunner var2);

    public void testFailed(GameTestInfo var1, GameTestRunner var2);

    public void testAddedForRerun(GameTestInfo var1, GameTestInfo var2, GameTestRunner var3);
}

