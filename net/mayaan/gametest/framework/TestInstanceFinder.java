/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest.framework;

import java.util.stream.Stream;
import net.mayaan.core.Holder;
import net.mayaan.gametest.framework.GameTestInstance;

@FunctionalInterface
public interface TestInstanceFinder {
    public Stream<Holder.Reference<GameTestInstance>> findTests();
}

