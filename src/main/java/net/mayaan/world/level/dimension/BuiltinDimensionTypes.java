/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.dimension;

import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.dimension.DimensionType;

public class BuiltinDimensionTypes {
    public static final ResourceKey<DimensionType> OVERWORLD = BuiltinDimensionTypes.register("overworld");
    public static final ResourceKey<DimensionType> NETHER = BuiltinDimensionTypes.register("the_nether");
    public static final ResourceKey<DimensionType> END = BuiltinDimensionTypes.register("the_end");
    public static final ResourceKey<DimensionType> OVERWORLD_CAVES = BuiltinDimensionTypes.register("overworld_caves");

    private static ResourceKey<DimensionType> register(String id) {
        return ResourceKey.create(Registries.DIMENSION_TYPE, Identifier.withDefaultNamespace(id));
    }
}

