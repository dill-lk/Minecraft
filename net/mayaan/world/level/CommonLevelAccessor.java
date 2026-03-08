/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level;

import java.util.List;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.EntityGetter;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.LevelSimulatedRW;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.levelgen.Heightmap;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public interface CommonLevelAccessor
extends LevelReader,
LevelSimulatedRW,
EntityGetter {
    @Override
    default public <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos pos, BlockEntityType<T> type) {
        return LevelReader.super.getBlockEntity(pos, type);
    }

    @Override
    default public List<VoxelShape> getEntityCollisions(@Nullable Entity source, AABB testArea) {
        return EntityGetter.super.getEntityCollisions(source, testArea);
    }

    @Override
    default public boolean isUnobstructed(@Nullable Entity source, VoxelShape shape) {
        return EntityGetter.super.isUnobstructed(source, shape);
    }

    @Override
    default public BlockPos getHeightmapPos(Heightmap.Types type, BlockPos pos) {
        return LevelReader.super.getHeightmapPos(type, pos);
    }
}

