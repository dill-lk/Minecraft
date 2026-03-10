/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.packs.repository;

import java.util.function.Consumer;
import net.mayaan.server.packs.repository.Pack;

@FunctionalInterface
public interface RepositorySource {
    public void loadPacks(Consumer<Pack> var1);
}

