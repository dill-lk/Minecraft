/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.internalapi;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface MayaanExecutorService {
    public <V> CompletableFuture<V> submit(Supplier<V> var1);

    public CompletableFuture<Void> submit(Runnable var1);
}

