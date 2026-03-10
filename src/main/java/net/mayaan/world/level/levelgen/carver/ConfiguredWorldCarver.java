/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.levelgen.carver;

import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.mayaan.SharedConstants;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderSet;
import net.mayaan.core.RegistryCodecs;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.RegistryFileCodec;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.chunk.CarvingMask;
import net.mayaan.world.level.chunk.ChunkAccess;
import net.mayaan.world.level.levelgen.Aquifer;
import net.mayaan.world.level.levelgen.carver.CarverConfiguration;
import net.mayaan.world.level.levelgen.carver.CarvingContext;
import net.mayaan.world.level.levelgen.carver.WorldCarver;

public record ConfiguredWorldCarver<WC extends CarverConfiguration>(WorldCarver<WC> worldCarver, WC config) {
    public static final Codec<ConfiguredWorldCarver<?>> DIRECT_CODEC = BuiltInRegistries.CARVER.byNameCodec().dispatch(c -> c.worldCarver, WorldCarver::configuredCodec);
    public static final Codec<Holder<ConfiguredWorldCarver<?>>> CODEC = RegistryFileCodec.create(Registries.CONFIGURED_CARVER, DIRECT_CODEC);
    public static final Codec<HolderSet<ConfiguredWorldCarver<?>>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.CONFIGURED_CARVER, DIRECT_CODEC);

    public boolean isStartChunk(RandomSource random) {
        return this.worldCarver.isStartChunk(this.config, random);
    }

    public boolean carve(CarvingContext context, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> biomeGetter, RandomSource random, Aquifer aquifer, ChunkPos sourceChunkPos, CarvingMask mask) {
        if (SharedConstants.debugVoidTerrain(chunk.getPos())) {
            return false;
        }
        return this.worldCarver.carve(context, this.config, chunk, biomeGetter, random, aquifer, sourceChunkPos, mask);
    }
}

