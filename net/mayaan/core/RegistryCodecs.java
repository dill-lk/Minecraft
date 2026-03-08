/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.core;

import com.mojang.serialization.Codec;
import net.mayaan.core.HolderSet;
import net.mayaan.core.Registry;
import net.mayaan.resources.HolderSetCodec;
import net.mayaan.resources.RegistryFileCodec;
import net.mayaan.resources.RegistryFixedCodec;
import net.mayaan.resources.ResourceKey;

public class RegistryCodecs {
    public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec) {
        return RegistryCodecs.homogeneousList(registryKey, elementCodec, false);
    }

    public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec, boolean alwaysUseList) {
        return HolderSetCodec.create(registryKey, RegistryFileCodec.create(registryKey, elementCodec), alwaysUseList);
    }

    public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey) {
        return RegistryCodecs.homogeneousList(registryKey, false);
    }

    public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, boolean alwaysUseList) {
        return HolderSetCodec.create(registryKey, RegistryFixedCodec.create(registryKey), alwaysUseList);
    }
}

