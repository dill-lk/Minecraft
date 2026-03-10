/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.dimension;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.dimension.DimensionType;

public record LevelStem(Holder<DimensionType> type, ChunkGenerator generator) {
    public static final Codec<LevelStem> CODEC = RecordCodecBuilder.create(i -> i.group((App)DimensionType.CODEC.fieldOf("type").forGetter(LevelStem::type), (App)ChunkGenerator.CODEC.fieldOf("generator").forGetter(LevelStem::generator)).apply((Applicative)i, i.stable(LevelStem::new)));
    public static final ResourceKey<LevelStem> OVERWORLD = ResourceKey.create(Registries.LEVEL_STEM, Identifier.withDefaultNamespace("overworld"));
    public static final ResourceKey<LevelStem> NETHER = ResourceKey.create(Registries.LEVEL_STEM, Identifier.withDefaultNamespace("the_nether"));
    public static final ResourceKey<LevelStem> END = ResourceKey.create(Registries.LEVEL_STEM, Identifier.withDefaultNamespace("the_end"));
}

