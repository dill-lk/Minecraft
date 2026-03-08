/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest.framework;

import net.mayaan.gametest.framework.GameTestInfo;
import net.mayaan.gametest.framework.LogTestReporter;
import net.mayaan.gametest.framework.TestReporter;

public class GlobalTestReporter {
    private static TestReporter DELEGATE = new LogTestReporter();

    public static void replaceWith(TestReporter testReporter) {
        DELEGATE = testReporter;
    }

    public static void onTestFailed(GameTestInfo testInfo) {
        DELEGATE.onTestFailed(testInfo);
    }

    public static void onTestSuccess(GameTestInfo testInfo) {
        DELEGATE.onTestSuccess(testInfo);
    }

    public static void finish() {
        DELEGATE.finish();
    }
}

