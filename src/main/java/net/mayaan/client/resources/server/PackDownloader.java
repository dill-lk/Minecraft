/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources.server;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import net.mayaan.server.packs.DownloadQueue;

public interface PackDownloader {
    public void download(Map<UUID, DownloadQueue.DownloadRequest> var1, Consumer<DownloadQueue.BatchResult> var2);
}

