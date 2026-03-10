/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.resources;

import net.mayaan.resources.ResourceKey;

@FunctionalInterface
public interface DependantName<T, V> {
    public V get(ResourceKey<T> var1);

    public static <T, V> DependantName<T, V> fixed(V value) {
        return id -> value;
    }
}

