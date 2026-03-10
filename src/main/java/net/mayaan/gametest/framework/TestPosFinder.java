/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest.framework;

import java.util.stream.Stream;
import net.mayaan.core.BlockPos;

@FunctionalInterface
public interface TestPosFinder {
    public Stream<BlockPos> findTestPos();
}

