/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.pathfinder;

import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.level.CollisionGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.pathfinder.PathType;
import net.mayaan.world.level.pathfinder.PathTypeCache;
import net.mayaan.world.level.pathfinder.WalkNodeEvaluator;
import org.jspecify.annotations.Nullable;

public class PathfindingContext {
    private final CollisionGetter level;
    private final @Nullable PathTypeCache cache;
    private final BlockPos mobPosition;
    private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

    public PathfindingContext(CollisionGetter level, Mob mob) {
        this.level = level;
        Level level2 = mob.level();
        if (level2 instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level2;
            this.cache = serverLevel.getPathTypeCache();
        } else {
            this.cache = null;
        }
        this.mobPosition = mob.blockPosition();
    }

    public PathType getPathTypeFromState(int x, int y, int z) {
        BlockPos.MutableBlockPos pos = this.mutablePos.set(x, y, z);
        if (this.cache == null) {
            return WalkNodeEvaluator.getPathTypeFromState(this.level, pos);
        }
        return this.cache.getOrCompute(this.level, pos);
    }

    public BlockState getBlockState(BlockPos pos) {
        return this.level.getBlockState(pos);
    }

    public CollisionGetter level() {
        return this.level;
    }

    public BlockPos mobPosition() {
        return this.mobPosition;
    }
}

