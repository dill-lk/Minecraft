/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.storage;

import net.mayaan.world.level.levelgen.WorldDimensions;
import net.mayaan.world.level.levelgen.WorldGenSettings;
import net.mayaan.world.level.storage.WorldData;

public record LevelDataAndDimensions(WorldDataAndGenSettings worldDataAndGenSettings, WorldDimensions.Complete dimensions) {
    public static LevelDataAndDimensions create(WorldData data, WorldGenSettings genSettings, WorldDimensions.Complete dimensions) {
        return new LevelDataAndDimensions(new WorldDataAndGenSettings(data, genSettings), dimensions);
    }

    public record WorldDataAndGenSettings(WorldData data, WorldGenSettings genSettings) {
    }
}

