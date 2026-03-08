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
import net.mayaan.data.worldgen.BastionBridgePools;
import net.mayaan.data.worldgen.BastionHoglinStablePools;
import net.mayaan.data.worldgen.BastionHousingUnitsPools;
import net.mayaan.data.worldgen.BastionSharedPools;
import net.mayaan.data.worldgen.BastionTreasureRoomPools;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.data.worldgen.Pools;
import net.mayaan.data.worldgen.ProcessorLists;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.levelgen.structure.pools.StructurePoolElement;
import net.mayaan.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class BastionPieces {
    public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("bastion/starts");

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
        Holder.Reference<StructureProcessorList> bastionGenericDegradation = processorLists.getOrThrow(ProcessorLists.BASTION_GENERIC_DEGRADATION);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        Holder.Reference<StructureTemplatePool> empty = pools.getOrThrow(Pools.EMPTY);
        context.register(START, new StructureTemplatePool(empty, (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of((Object)Pair.of(StructurePoolElement.single("bastion/units/air_base", bastionGenericDegradation), (Object)1), (Object)Pair.of(StructurePoolElement.single("bastion/hoglin_stable/air_base", bastionGenericDegradation), (Object)1), (Object)Pair.of(StructurePoolElement.single("bastion/treasure/big_air_full", bastionGenericDegradation), (Object)1), (Object)Pair.of(StructurePoolElement.single("bastion/bridge/starting_pieces/entrance_base", bastionGenericDegradation), (Object)1)), StructureTemplatePool.Projection.RIGID));
        BastionHousingUnitsPools.bootstrap(context);
        BastionHoglinStablePools.bootstrap(context);
        BastionTreasureRoomPools.bootstrap(context);
        BastionBridgePools.bootstrap(context);
        BastionSharedPools.bootstrap(context);
    }
}

