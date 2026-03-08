/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.packs.resources;

import net.mayaan.server.packs.resources.ResourceManager;

public interface CloseableResourceManager
extends ResourceManager,
AutoCloseable {
    @Override
    public void close();
}

