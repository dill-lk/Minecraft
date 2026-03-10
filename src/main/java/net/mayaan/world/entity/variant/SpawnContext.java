/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.variant;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.world.attribute.EnvironmentAttributeReader;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.biome.Biome;

public record SpawnContext(BlockPos pos, ServerLevelAccessor level, EnvironmentAttributeReader environmentAttributes, Holder<Biome> biome) {
    public static SpawnContext create(ServerLevelAccessor level, BlockPos pos) {
        Holder<Biome> biome = level.getBiome(pos);
        return new SpawnContext(pos, level, level.environmentAttributes(), biome);
    }
}

