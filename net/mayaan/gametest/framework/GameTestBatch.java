/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest.framework;

import java.util.Collection;
import net.mayaan.core.Holder;
import net.mayaan.gametest.framework.GameTestInfo;
import net.mayaan.gametest.framework.TestEnvironmentDefinition;

public record GameTestBatch(int index, Collection<GameTestInfo> gameTestInfos, Holder<TestEnvironmentDefinition<?>> environment) {
    public GameTestBatch {
        if (gameTestInfos.isEmpty()) {
            throw new IllegalArgumentException("A GameTestBatch must include at least one GameTestInfo!");
        }
    }
}

