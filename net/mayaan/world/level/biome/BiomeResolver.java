/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.biome;

import net.mayaan.core.Holder;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.biome.Climate;

public interface BiomeResolver {
    public Holder<Biome> getNoiseBiome(int var1, int var2, int var3, Climate.Sampler var4);
}

