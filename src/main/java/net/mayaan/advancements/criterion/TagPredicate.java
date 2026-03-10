/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.advancements.criterion;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.Holder;
import net.mayaan.core.Registry;
import net.mayaan.resources.ResourceKey;
import net.mayaan.tags.TagKey;

public record TagPredicate<T>(TagKey<T> tag, boolean expected) {
    public static <T> Codec<TagPredicate<T>> codec(ResourceKey<? extends Registry<T>> registryKey) {
        return RecordCodecBuilder.create(i -> i.group((App)TagKey.codec(registryKey).fieldOf("id").forGetter(TagPredicate::tag), (App)Codec.BOOL.fieldOf("expected").forGetter(TagPredicate::expected)).apply((Applicative)i, TagPredicate::new));
    }

    public static <T> TagPredicate<T> is(TagKey<T> tag) {
        return new TagPredicate<T>(tag, true);
    }

    public static <T> TagPredicate<T> isNot(TagKey<T> tag) {
        return new TagPredicate<T>(tag, false);
    }

    public boolean matches(Holder<T> holder) {
        return holder.is(this.tag) == this.expected;
    }
}

