/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.levelgen.structure.pools.alias;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.ResourceKey;
import net.mayaan.util.RandomSource;
import net.mayaan.util.random.Weighted;
import net.mayaan.util.random.WeightedList;
import net.mayaan.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.mayaan.world.level.levelgen.structure.pools.alias.PoolAliasBinding;

public record RandomPoolAlias(ResourceKey<StructureTemplatePool> alias, WeightedList<ResourceKey<StructureTemplatePool>> targets) implements PoolAliasBinding
{
    static final MapCodec<RandomPoolAlias> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.TEMPLATE_POOL).fieldOf("alias").forGetter(RandomPoolAlias::alias), (App)WeightedList.nonEmptyCodec(ResourceKey.codec(Registries.TEMPLATE_POOL)).fieldOf("targets").forGetter(RandomPoolAlias::targets)).apply((Applicative)i, RandomPoolAlias::new));

    @Override
    public void forEachResolved(RandomSource random, BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> aliasAndTargetConsumer) {
        this.targets.getRandom(random).ifPresent(target -> aliasAndTargetConsumer.accept(this.alias, (ResourceKey<StructureTemplatePool>)target));
    }

    @Override
    public Stream<ResourceKey<StructureTemplatePool>> allTargets() {
        return this.targets.unwrap().stream().map(Weighted::value);
    }

    public MapCodec<RandomPoolAlias> codec() {
        return CODEC;
    }
}

