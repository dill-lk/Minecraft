/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.crafting.display;

import net.mayaan.core.HolderLookup;
import net.mayaan.util.context.ContextKey;
import net.mayaan.util.context.ContextKeySet;
import net.mayaan.util.context.ContextMap;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.entity.FuelValues;

public class SlotDisplayContext {
    public static final ContextKey<FuelValues> FUEL_VALUES = ContextKey.vanilla("fuel_values");
    public static final ContextKey<HolderLookup.Provider> REGISTRIES = ContextKey.vanilla("registries");
    public static final ContextKeySet CONTEXT = new ContextKeySet.Builder().optional(FUEL_VALUES).optional(REGISTRIES).build();

    public static ContextMap fromLevel(Level level) {
        return new ContextMap.Builder().withParameter(FUEL_VALUES, level.fuelValues()).withParameter(REGISTRIES, level.registryAccess()).create(CONTEXT);
    }
}

