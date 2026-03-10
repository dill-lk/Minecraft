/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server;

import net.mayaan.core.LayeredRegistryAccess;
import net.mayaan.server.RegistryLayer;
import net.mayaan.server.ReloadableServerResources;
import net.mayaan.server.packs.resources.CloseableResourceManager;
import net.mayaan.world.level.storage.LevelDataAndDimensions;

public record WorldStem(CloseableResourceManager resourceManager, ReloadableServerResources dataPackResources, LayeredRegistryAccess<RegistryLayer> registries, LevelDataAndDimensions.WorldDataAndGenSettings worldDataAndGenSettings) implements AutoCloseable
{
    @Override
    public void close() {
        this.resourceManager.close();
    }
}

