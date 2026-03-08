/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.chunk;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.Registry;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.levelgen.DebugLevelSource;
import net.mayaan.world.level.levelgen.FlatLevelSource;
import net.mayaan.world.level.levelgen.NoiseBasedChunkGenerator;

public class ChunkGenerators {
    public static MapCodec<? extends ChunkGenerator> bootstrap(Registry<MapCodec<? extends ChunkGenerator>> registry) {
        Registry.register(registry, "noise", NoiseBasedChunkGenerator.CODEC);
        Registry.register(registry, "flat", FlatLevelSource.CODEC);
        return Registry.register(registry, "debug", DebugLevelSource.CODEC);
    }
}

