/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.structure.pools.alias;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.mayaan.core.Holder;
import net.mayaan.core.Registry;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.data.worldgen.Pools;
import net.mayaan.world.level.levelgen.structure.pools.StructurePoolElement;
import net.mayaan.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.mayaan.world.level.levelgen.structure.pools.alias.DirectPoolAlias;
import net.mayaan.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.mayaan.world.level.levelgen.structure.pools.alias.RandomGroupPoolAlias;
import net.mayaan.world.level.levelgen.structure.pools.alias.RandomPoolAlias;

public class PoolAliasBindings {
    public static MapCodec<? extends PoolAliasBinding> bootstrap(Registry<MapCodec<? extends PoolAliasBinding>> registry) {
        Registry.register(registry, "random", RandomPoolAlias.CODEC);
        Registry.register(registry, "random_group", RandomGroupPoolAlias.CODEC);
        return Registry.register(registry, "direct", DirectPoolAlias.CODEC);
    }

    public static void registerTargetsAsPools(BootstrapContext<StructureTemplatePool> context, Holder<StructureTemplatePool> emptyPool, List<PoolAliasBinding> aliasBindings) {
        aliasBindings.stream().flatMap(PoolAliasBinding::allTargets).map(key -> key.identifier().getPath()).forEach(path -> Pools.register(context, path, new StructureTemplatePool(emptyPool, List.of(Pair.of(StructurePoolElement.single(path), (Object)1)), StructureTemplatePool.Projection.RIGID)));
    }
}

