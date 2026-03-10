/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest;

import net.mayaan.SharedConstants;
import net.mayaan.gametest.framework.GameTestMainUtil;

public class Main {
    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        GameTestMainUtil.runGameTestServer(args, path -> {});
    }
}

