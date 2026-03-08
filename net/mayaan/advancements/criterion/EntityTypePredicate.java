/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.advancements.criterion;

import com.mojang.serialization.Codec;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.HolderSet;
import net.mayaan.core.RegistryCodecs;
import net.mayaan.core.registries.Registries;
import net.mayaan.tags.TagKey;
import net.mayaan.world.entity.EntityType;

public record EntityTypePredicate(HolderSet<EntityType<?>> types) {
    public static final Codec<EntityTypePredicate> CODEC = RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).xmap(EntityTypePredicate::new, EntityTypePredicate::types);

    public static EntityTypePredicate of(HolderGetter<EntityType<?>> lookup, EntityType<?> type) {
        return new EntityTypePredicate(HolderSet.direct(type.builtInRegistryHolder()));
    }

    public static EntityTypePredicate of(HolderGetter<EntityType<?>> lookup, TagKey<EntityType<?>> type) {
        return new EntityTypePredicate(lookup.getOrThrow(type));
    }

    public boolean matches(Holder<EntityType<?>> type) {
        return this.types.contains(type);
    }
}

