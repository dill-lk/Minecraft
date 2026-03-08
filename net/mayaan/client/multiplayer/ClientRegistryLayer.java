/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.multiplayer;

import java.util.List;
import net.mayaan.core.LayeredRegistryAccess;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.registries.BuiltInRegistries;

public enum ClientRegistryLayer {
    STATIC,
    REMOTE;

    private static final List<ClientRegistryLayer> VALUES;
    private static final RegistryAccess.Frozen STATIC_ACCESS;

    public static LayeredRegistryAccess<ClientRegistryLayer> createRegistryAccess() {
        return new LayeredRegistryAccess<ClientRegistryLayer>(VALUES).replaceFrom(STATIC, STATIC_ACCESS);
    }

    static {
        VALUES = List.of(ClientRegistryLayer.values());
        STATIC_ACCESS = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    }
}

