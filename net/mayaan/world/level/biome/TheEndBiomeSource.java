/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.biome;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.QuartPos;
import net.mayaan.core.SectionPos;
import net.mayaan.resources.RegistryOps;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.biome.BiomeSource;
import net.mayaan.world.level.biome.Biomes;
import net.mayaan.world.level.biome.Climate;
import net.mayaan.world.level.levelgen.DensityFunction;

public class TheEndBiomeSource
extends BiomeSource {
    public static final MapCodec<TheEndBiomeSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(RegistryOps.retrieveElement(Biomes.THE_END), RegistryOps.retrieveElement(Biomes.END_HIGHLANDS), RegistryOps.retrieveElement(Biomes.END_MIDLANDS), RegistryOps.retrieveElement(Biomes.SMALL_END_ISLANDS), RegistryOps.retrieveElement(Biomes.END_BARRENS)).apply((Applicative)i, i.stable(TheEndBiomeSource::new)));
    private final Holder<Biome> end;
    private final Holder<Biome> highlands;
    private final Holder<Biome> midlands;
    private final Holder<Biome> islands;
    private final Holder<Biome> barrens;

    public static TheEndBiomeSource create(HolderGetter<Biome> biomes) {
        return new TheEndBiomeSource(biomes.getOrThrow(Biomes.THE_END), biomes.getOrThrow(Biomes.END_HIGHLANDS), biomes.getOrThrow(Biomes.END_MIDLANDS), biomes.getOrThrow(Biomes.SMALL_END_ISLANDS), biomes.getOrThrow(Biomes.END_BARRENS));
    }

    private TheEndBiomeSource(Holder<Biome> end, Holder<Biome> highlands, Holder<Biome> midlands, Holder<Biome> islands, Holder<Biome> barrens) {
        this.end = end;
        this.highlands = highlands;
        this.midlands = midlands;
        this.islands = islands;
        this.barrens = barrens;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.of(this.end, this.highlands, this.midlands, this.islands, this.barrens);
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) {
        int chunkZ;
        int blockX = QuartPos.toBlock(quartX);
        int blockY = QuartPos.toBlock(quartY);
        int blockZ = QuartPos.toBlock(quartZ);
        int chunkX = SectionPos.blockToSectionCoord(blockX);
        if ((long)chunkX * (long)chunkX + (long)(chunkZ = SectionPos.blockToSectionCoord(blockZ)) * (long)chunkZ <= 4096L) {
            return this.end;
        }
        int weirdBlockX = (SectionPos.blockToSectionCoord(blockX) * 2 + 1) * 8;
        int weirdBlockZ = (SectionPos.blockToSectionCoord(blockZ) * 2 + 1) * 8;
        double heightValue = sampler.erosion().compute(new DensityFunction.SinglePointContext(weirdBlockX, blockY, weirdBlockZ));
        if (heightValue > 0.25) {
            return this.highlands;
        }
        if (heightValue >= -0.0625) {
            return this.midlands;
        }
        if (heightValue < -0.21875) {
            return this.islands;
        }
        return this.barrens;
    }
}

