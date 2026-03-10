/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level;

import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.LevelAccessor;

public interface ServerLevelAccessor
extends LevelAccessor {
    public ServerLevel getLevel();

    public DifficultyInstance getCurrentDifficultyAt(BlockPos var1);

    default public void addFreshEntityWithPassengers(Entity entity) {
        entity.getSelfAndPassengers().forEach(this::addFreshEntity);
    }
}

