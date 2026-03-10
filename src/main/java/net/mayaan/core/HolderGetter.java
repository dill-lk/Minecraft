/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.core;

import java.util.Optional;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderSet;
import net.mayaan.core.Registry;
import net.mayaan.resources.ResourceKey;
import net.mayaan.tags.TagKey;
import net.mayaan.util.RandomSource;

public interface HolderGetter<T> {
    public Optional<Holder.Reference<T>> get(ResourceKey<T> var1);

    default public Holder.Reference<T> getOrThrow(ResourceKey<T> id) {
        return this.get(id).orElseThrow(() -> new IllegalStateException("Missing element " + String.valueOf(id)));
    }

    public Optional<HolderSet.Named<T>> get(TagKey<T> var1);

    default public HolderSet.Named<T> getOrThrow(TagKey<T> id) {
        return this.get(id).orElseThrow(() -> new IllegalStateException("Missing tag " + String.valueOf(id)));
    }

    default public Optional<Holder<T>> getRandomElementOf(TagKey<T> tag, RandomSource random) {
        return this.get(tag).flatMap(holderSet -> holderSet.getRandomElement(random));
    }

    public static interface Provider {
        public <T> Optional<? extends HolderGetter<T>> lookup(ResourceKey<? extends Registry<? extends T>> var1);

        default public <T> HolderGetter<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> key) {
            return this.lookup(key).orElseThrow(() -> new IllegalStateException("Registry " + String.valueOf(key.identifier()) + " not found"));
        }

        default public <T> Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
            return this.lookup(id.registryKey()).flatMap(l -> l.get(id));
        }

        default public <T> Holder.Reference<T> getOrThrow(ResourceKey<T> id) {
            return (Holder.Reference)this.lookup(id.registryKey()).flatMap(l -> l.get(id)).orElseThrow(() -> new IllegalStateException("Missing element " + String.valueOf(id)));
        }
    }
}

