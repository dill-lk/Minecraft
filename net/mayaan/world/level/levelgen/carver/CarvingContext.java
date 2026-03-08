/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.levelgen.carver;

import java.util.Optional;
import java.util.function.Function;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.RegistryAccess;
import net.mayaan.world.level.LevelHeightAccessor;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.chunk.ChunkAccess;
import net.mayaan.world.level.levelgen.NoiseBasedChunkGenerator;
import net.mayaan.world.level.levelgen.NoiseChunk;
import net.mayaan.world.level.levelgen.RandomState;
import net.mayaan.world.level.levelgen.SurfaceRules;
import net.mayaan.world.level.levelgen.WorldGenerationContext;

public class CarvingContext
extends WorldGenerationContext {
    private final RegistryAccess registryAccess;
    private final NoiseChunk noiseChunk;
    private final RandomState randomState;
    private final SurfaceRules.RuleSource surfaceRule;

    public CarvingContext(NoiseBasedChunkGenerator generator, RegistryAccess registryAccess, LevelHeightAccessor heightAccessor, NoiseChunk noiseChunk, RandomState randomState, SurfaceRules.RuleSource surfaceRule) {
        super(generator, heightAccessor);
        this.registryAccess = registryAccess;
        this.noiseChunk = noiseChunk;
        this.randomState = randomState;
        this.surfaceRule = surfaceRule;
    }

    @Deprecated
    public Optional<BlockState> topMaterial(Function<BlockPos, Holder<Biome>> biomeGetter, ChunkAccess chunk, BlockPos pos, boolean underFluid) {
        return this.randomState.surfaceSystem().topMaterial(this.surfaceRule, this, biomeGetter, chunk, this.noiseChunk, pos, underFluid);
    }

    @Deprecated
    public RegistryAccess registryAccess() {
        return this.registryAccess;
    }

    public RandomState randomState() {
        return this.randomState;
    }
}

