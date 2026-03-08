/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util;

import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.Vec3i;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.tags.BlockTags;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.LeavesBlock;
import net.mayaan.world.level.block.StainedGlassBlock;
import net.mayaan.world.level.block.StainedGlassPaneBlock;
import net.mayaan.world.level.block.state.BlockState;

public class SpawnUtil {
    public static <T extends Mob> Optional<T> trySpawnMob(EntityType<T> entityType, EntitySpawnReason spawnReason, ServerLevel level, BlockPos start, int spawnAttempts, int spawnRangeXZ, int spawnRangeY, Strategy strategy, boolean checkCollisions) {
        BlockPos.MutableBlockPos searchPos = start.mutable();
        RandomSource random = level.getRandom();
        for (int i = 0; i < spawnAttempts; ++i) {
            Mob mob;
            int dx = Mth.randomBetweenInclusive(random, -spawnRangeXZ, spawnRangeXZ);
            int dz = Mth.randomBetweenInclusive(random, -spawnRangeXZ, spawnRangeXZ);
            searchPos.setWithOffset(start, dx, spawnRangeY, dz);
            if (!level.getWorldBorder().isWithinBounds(searchPos) || !SpawnUtil.moveToPossibleSpawnPosition(level, spawnRangeY, searchPos, strategy) || checkCollisions && !level.noCollision(entityType.getSpawnAABB((double)searchPos.getX() + 0.5, searchPos.getY(), (double)searchPos.getZ() + 0.5)) || (mob = (Mob)entityType.create(level, null, searchPos, spawnReason, false, false)) == null) continue;
            if (mob.checkSpawnRules(level, spawnReason) && mob.checkSpawnObstruction(level)) {
                level.addFreshEntityWithPassengers(mob);
                mob.playAmbientSound();
                return Optional.of(mob);
            }
            mob.discard();
        }
        return Optional.empty();
    }

    private static boolean moveToPossibleSpawnPosition(ServerLevel level, int spawnRangeY, BlockPos.MutableBlockPos searchPos, Strategy strategy) {
        BlockPos.MutableBlockPos abovePos = new BlockPos.MutableBlockPos().set(searchPos);
        BlockState aboveState = level.getBlockState(abovePos);
        for (int y = spawnRangeY; y >= -spawnRangeY; --y) {
            searchPos.move(Direction.DOWN);
            abovePos.setWithOffset((Vec3i)searchPos, Direction.UP);
            BlockState currentState = level.getBlockState(searchPos);
            if (strategy.canSpawnOn(level, searchPos, currentState, abovePos, aboveState)) {
                searchPos.move(Direction.UP);
                return true;
            }
            aboveState = currentState;
        }
        return false;
    }

    public static interface Strategy {
        @Deprecated
        public static final Strategy LEGACY_IRON_GOLEM = (level, pos, blockState, abovePos, aboveState) -> {
            if (blockState.is(Blocks.COBWEB) || blockState.is(Blocks.CACTUS) || blockState.is(Blocks.GLASS_PANE) || blockState.getBlock() instanceof StainedGlassPaneBlock || blockState.getBlock() instanceof StainedGlassBlock || blockState.getBlock() instanceof LeavesBlock || blockState.is(Blocks.CONDUIT) || blockState.is(Blocks.ICE) || blockState.is(Blocks.TNT) || blockState.is(Blocks.GLOWSTONE) || blockState.is(Blocks.BEACON) || blockState.is(Blocks.SEA_LANTERN) || blockState.is(Blocks.FROSTED_ICE) || blockState.is(Blocks.TINTED_GLASS) || blockState.is(Blocks.GLASS)) {
                return false;
            }
            return !(!aboveState.isAir() && !aboveState.liquid() || !blockState.isSolid() && !blockState.is(Blocks.POWDER_SNOW));
        };
        public static final Strategy ON_TOP_OF_COLLIDER = (level, pos, blockState, abovePos, aboveState) -> aboveState.getCollisionShape(level, abovePos).isEmpty() && Block.isFaceFull(blockState.getCollisionShape(level, pos), Direction.UP);
        public static final Strategy ON_TOP_OF_COLLIDER_NO_LEAVES = (level, pos, blockState, abovePos, aboveState) -> aboveState.getCollisionShape(level, abovePos).isEmpty() && !blockState.is(BlockTags.LEAVES) && Block.isFaceFull(blockState.getCollisionShape(level, pos), Direction.UP);

        public boolean canSpawnOn(ServerLevel var1, BlockPos var2, BlockState var3, BlockPos var4, BlockState var5);
    }
}

