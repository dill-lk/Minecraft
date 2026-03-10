/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity;

import net.mayaan.core.BlockPos;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.level.LevelReader;
import org.jspecify.annotations.Nullable;

public interface SpawnPlacementType {
    public boolean isSpawnPositionOk(LevelReader var1, BlockPos var2, @Nullable EntityType<?> var3);

    default public BlockPos adjustSpawnPosition(LevelReader level, BlockPos candidate) {
        return candidate;
    }
}

