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
import net.mayaan.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.mayaan.world.level.levelgen.structure.pools.alias.PoolAliasBinding;

public record DirectPoolAlias(ResourceKey<StructureTemplatePool> alias, ResourceKey<StructureTemplatePool> target) implements PoolAliasBinding
{
    static final MapCodec<DirectPoolAlias> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.TEMPLATE_POOL).fieldOf("alias").forGetter(DirectPoolAlias::alias), (App)ResourceKey.codec(Registries.TEMPLATE_POOL).fieldOf("target").forGetter(DirectPoolAlias::target)).apply((Applicative)i, DirectPoolAlias::new));

    @Override
    public void forEachResolved(RandomSource random, BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> aliasAndTargetConsumer) {
        aliasAndTargetConsumer.accept(this.alias, this.target);
    }

    @Override
    public Stream<ResourceKey<StructureTemplatePool>> allTargets() {
        return Stream.of(this.target);
    }

    public MapCodec<DirectPoolAlias> codec() {
        return CODEC;
    }
}

