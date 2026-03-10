/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.mojang.datafixers.util.Pair
 */
package net.mayaan.data.worldgen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.function.Function;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.data.worldgen.Pools;
import net.mayaan.world.level.levelgen.structure.pools.StructurePoolElement;
import net.mayaan.world.level.levelgen.structure.pools.StructureTemplatePool;

public class BastionSharedPools {
    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        Holder.Reference<StructureTemplatePool> empty = pools.getOrThrow(Pools.EMPTY);
        Pools.register(context, "bastion/mobs/piglin", new StructureTemplatePool(empty, (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of((Object)Pair.of(StructurePoolElement.single("bastion/mobs/melee_piglin"), (Object)1), (Object)Pair.of(StructurePoolElement.single("bastion/mobs/sword_piglin"), (Object)4), (Object)Pair.of(StructurePoolElement.single("bastion/mobs/crossbow_piglin"), (Object)4), (Object)Pair.of(StructurePoolElement.single("bastion/mobs/empty"), (Object)1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(context, "bastion/mobs/hoglin", new StructureTemplatePool(empty, (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of((Object)Pair.of(StructurePoolElement.single("bastion/mobs/hoglin"), (Object)2), (Object)Pair.of(StructurePoolElement.single("bastion/mobs/empty"), (Object)1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(context, "bastion/blocks/gold", new StructureTemplatePool(empty, (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of((Object)Pair.of(StructurePoolElement.single("bastion/blocks/air"), (Object)3), (Object)Pair.of(StructurePoolElement.single("bastion/blocks/gold"), (Object)1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(context, "bastion/mobs/piglin_melee", new StructureTemplatePool(empty, (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of((Object)Pair.of(StructurePoolElement.single("bastion/mobs/melee_piglin_always"), (Object)1), (Object)Pair.of(StructurePoolElement.single("bastion/mobs/melee_piglin"), (Object)5), (Object)Pair.of(StructurePoolElement.single("bastion/mobs/sword_piglin"), (Object)1)), StructureTemplatePool.Projection.RIGID));
    }
}

