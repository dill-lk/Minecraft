/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.tags;

import java.util.concurrent.CompletableFuture;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.Registry;
import net.mayaan.data.PackOutput;
import net.mayaan.data.tags.TagAppender;
import net.mayaan.data.tags.TagsProvider;
import net.mayaan.resources.ResourceKey;
import net.mayaan.tags.TagBuilder;
import net.mayaan.tags.TagKey;

public abstract class KeyTagProvider<T>
extends TagsProvider<T> {
    protected KeyTagProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, registryKey, lookupProvider);
    }

    protected TagAppender<ResourceKey<T>, T> tag(TagKey<T> tag) {
        TagBuilder builder = this.getOrCreateRawBuilder(tag);
        return TagAppender.forBuilder(builder);
    }

    protected TagAppender<ResourceKey<T>, T> tag(TagKey<T> tag, boolean replace) {
        TagBuilder builder = this.getOrCreateRawBuilder(tag);
        builder.setReplace(replace);
        return TagAppender.forBuilder(builder);
    }
}

