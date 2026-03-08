/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Lifecycle
 */
package net.mayaan.data.worldgen;

import com.mojang.serialization.Lifecycle;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.Registry;
import net.mayaan.resources.ResourceKey;

public interface BootstrapContext<T> {
    public Holder.Reference<T> register(ResourceKey<T> var1, T var2, Lifecycle var3);

    default public Holder.Reference<T> register(ResourceKey<T> key, T value) {
        return this.register(key, value, Lifecycle.stable());
    }

    public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> var1);
}

