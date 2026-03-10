/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.worldselection;

import net.mayaan.client.gui.screens.worldselection.DataPackReloadCookie;
import net.mayaan.client.gui.screens.worldselection.WorldCreationContext;
import net.mayaan.core.LayeredRegistryAccess;
import net.mayaan.server.RegistryLayer;
import net.mayaan.server.ReloadableServerResources;

@FunctionalInterface
public interface WorldCreationContextMapper {
    public WorldCreationContext apply(ReloadableServerResources var1, LayeredRegistryAccess<RegistryLayer> var2, DataPackReloadCookie var3);
}

