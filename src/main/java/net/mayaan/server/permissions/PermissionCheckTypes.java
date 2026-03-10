/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.server.permissions;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.Registry;
import net.mayaan.resources.Identifier;
import net.mayaan.server.permissions.PermissionCheck;

public class PermissionCheckTypes {
    public static MapCodec<? extends PermissionCheck> bootstrap(Registry<MapCodec<? extends PermissionCheck>> registry) {
        Registry.register(registry, Identifier.withDefaultNamespace("always_pass"), PermissionCheck.AlwaysPass.MAP_CODEC);
        return Registry.register(registry, Identifier.withDefaultNamespace("require"), PermissionCheck.Require.MAP_CODEC);
    }
}

