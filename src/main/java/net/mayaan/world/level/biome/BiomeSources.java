/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.biome;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.Registry;
import net.mayaan.world.level.biome.BiomeSource;
import net.mayaan.world.level.biome.CheckerboardColumnBiomeSource;
import net.mayaan.world.level.biome.FixedBiomeSource;
import net.mayaan.world.level.biome.MultiNoiseBiomeSource;
import net.mayaan.world.level.biome.TheEndBiomeSource;

public class BiomeSources {
    public static MapCodec<? extends BiomeSource> bootstrap(Registry<MapCodec<? extends BiomeSource>> registry) {
        Registry.register(registry, "fixed", FixedBiomeSource.CODEC);
        Registry.register(registry, "multi_noise", MultiNoiseBiomeSource.CODEC);
        Registry.register(registry, "checkerboard", CheckerboardColumnBiomeSource.CODEC);
        return Registry.register(registry, "the_end", TheEndBiomeSource.CODEC);
    }
}

