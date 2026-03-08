/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.core;

import java.util.List;
import java.util.Map;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.RegistrationInfo;
import net.mayaan.core.Registry;
import net.mayaan.resources.ResourceKey;
import net.mayaan.tags.TagKey;

public interface WritableRegistry<T>
extends Registry<T> {
    public Holder.Reference<T> register(ResourceKey<T> var1, T var2, RegistrationInfo var3);

    public void bindTags(Map<TagKey<T>, List<Holder<T>>> var1);

    public boolean isEmpty();

    public HolderGetter<T> createRegistrationLookup();
}

