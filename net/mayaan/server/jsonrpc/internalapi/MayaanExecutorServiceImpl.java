/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.internalapi;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.mayaan.server.dedicated.DedicatedServer;
import net.mayaan.server.jsonrpc.internalapi.MayaanExecutorService;

public class MayaanExecutorServiceImpl
implements MayaanExecutorService {
    private final DedicatedServer server;

    public MayaanExecutorServiceImpl(DedicatedServer server) {
        this.server = server;
    }

    @Override
    public <V> CompletableFuture<V> submit(Supplier<V> supplier) {
        return this.server.submit(supplier);
    }

    @Override
    public CompletableFuture<Void> submit(Runnable runnable) {
        return this.server.submit(runnable);
    }
}

