/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level;

import java.util.Optional;
import java.util.function.Predicate;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.Heightmap;
import net.mayaan.world.level.material.FluidState;

public interface LevelSimulatedReader {
    public boolean isStateAtPosition(BlockPos var1, Predicate<BlockState> var2);

    public boolean isFluidAtPosition(BlockPos var1, Predicate<FluidState> var2);

    public <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos var1, BlockEntityType<T> var2);

    public BlockPos getHeightmapPos(Heightmap.Types var1, BlockPos var2);
}

