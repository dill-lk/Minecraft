/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.structure.pools;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.mayaan.world.level.levelgen.structure.pools.FeaturePoolElement;
import net.mayaan.world.level.levelgen.structure.pools.LegacySinglePoolElement;
import net.mayaan.world.level.levelgen.structure.pools.ListPoolElement;
import net.mayaan.world.level.levelgen.structure.pools.SinglePoolElement;
import net.mayaan.world.level.levelgen.structure.pools.StructurePoolElement;

public interface StructurePoolElementType<P extends StructurePoolElement> {
    public static final StructurePoolElementType<SinglePoolElement> SINGLE = StructurePoolElementType.register("single_pool_element", SinglePoolElement.CODEC);
    public static final StructurePoolElementType<ListPoolElement> LIST = StructurePoolElementType.register("list_pool_element", ListPoolElement.CODEC);
    public static final StructurePoolElementType<FeaturePoolElement> FEATURE = StructurePoolElementType.register("feature_pool_element", FeaturePoolElement.CODEC);
    public static final StructurePoolElementType<EmptyPoolElement> EMPTY = StructurePoolElementType.register("empty_pool_element", EmptyPoolElement.CODEC);
    public static final StructurePoolElementType<LegacySinglePoolElement> LEGACY = StructurePoolElementType.register("legacy_single_pool_element", LegacySinglePoolElement.CODEC);

    public MapCodec<P> codec();

    public static <P extends StructurePoolElement> StructurePoolElementType<P> register(String id, MapCodec<P> codec) {
        return Registry.register(BuiltInRegistries.STRUCTURE_POOL_ELEMENT, id, () -> codec);
    }
}

