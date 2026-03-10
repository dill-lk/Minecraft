/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.equipment;

import net.mayaan.core.HolderGetter;
import net.mayaan.core.HolderSet;
import net.mayaan.world.entity.EntityType;

@FunctionalInterface
public interface AllowedEntitiesProvider {
    public HolderSet<EntityType<?>> get(HolderGetter<EntityType<?>> var1);
}

