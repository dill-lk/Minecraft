/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server;

import java.util.List;
import net.mayaan.core.LayeredRegistryAccess;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.registries.BuiltInRegistries;

public enum RegistryLayer {
    STATIC,
    WORLDGEN,
    DIMENSIONS,
    RELOADABLE;

    private static final List<RegistryLayer> VALUES;
    private static final RegistryAccess.Frozen STATIC_ACCESS;

    public static LayeredRegistryAccess<RegistryLayer> createRegistryAccess() {
        return new LayeredRegistryAccess<RegistryLayer>(VALUES).replaceFrom(STATIC, STATIC_ACCESS);
    }

    static {
        VALUES = List.of(RegistryLayer.values());
        STATIC_ACCESS = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    }
}

