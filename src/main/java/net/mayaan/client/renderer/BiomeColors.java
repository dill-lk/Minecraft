/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer;

import net.mayaan.client.renderer.block.BlockAndTintGetter;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.ColorResolver;
import net.mayaan.world.level.biome.Biome;

public class BiomeColors {
    public static final ColorResolver GRASS_COLOR_RESOLVER = Biome::getGrassColor;
    public static final ColorResolver FOLIAGE_COLOR_RESOLVER = (biome, x, z) -> biome.getFoliageColor();
    public static final ColorResolver DRY_FOLIAGE_COLOR_RESOLVER = (biome, x, z) -> biome.getDryFoliageColor();
    public static final ColorResolver WATER_COLOR_RESOLVER = (biome, x, z) -> biome.getWaterColor();

    private static int getAverageColor(BlockAndTintGetter level, BlockPos pos, ColorResolver colorResolver) {
        return level.getBlockTint(pos, colorResolver);
    }

    public static int getAverageGrassColor(BlockAndTintGetter level, BlockPos pos) {
        return BiomeColors.getAverageColor(level, pos, GRASS_COLOR_RESOLVER);
    }

    public static int getAverageFoliageColor(BlockAndTintGetter level, BlockPos pos) {
        return BiomeColors.getAverageColor(level, pos, FOLIAGE_COLOR_RESOLVER);
    }

    public static int getAverageDryFoliageColor(BlockAndTintGetter level, BlockPos pos) {
        return BiomeColors.getAverageColor(level, pos, DRY_FOLIAGE_COLOR_RESOLVER);
    }

    public static int getAverageWaterColor(BlockAndTintGetter level, BlockPos pos) {
        return BiomeColors.getAverageColor(level, pos, WATER_COLOR_RESOLVER);
    }
}

