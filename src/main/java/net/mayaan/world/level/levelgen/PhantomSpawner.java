/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.levelgen;

import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.stats.ServerStatsCounter;
import net.mayaan.stats.Stats;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.SpawnGroupData;
import net.mayaan.world.entity.monster.Phantom;
import net.mayaan.world.level.CustomSpawner;
import net.mayaan.world.level.NaturalSpawner;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gamerules.GameRules;
import net.mayaan.world.level.material.FluidState;

public class PhantomSpawner
implements CustomSpawner {
    private int nextTick;

    @Override
    public void tick(ServerLevel level, boolean spawnEnemies) {
        if (!spawnEnemies) {
            return;
        }
        if (!level.getGameRules().get(GameRules.SPAWN_PHANTOMS).booleanValue()) {
            return;
        }
        RandomSource random = level.getRandom();
        --this.nextTick;
        if (this.nextTick > 0) {
            return;
        }
        this.nextTick += (60 + random.nextInt(60)) * 20;
        if (level.getSkyDarken() < 5 && level.dimensionType().hasSkyLight()) {
            return;
        }
        for (ServerPlayer player : level.players()) {
            FluidState fluidState;
            BlockState blockState;
            BlockPos spawnPos;
            DifficultyInstance difficulty;
            if (player.isSpectator()) continue;
            BlockPos playerPos = player.blockPosition();
            if (level.dimensionType().hasSkyLight() && (playerPos.getY() < level.getSeaLevel() || !level.canSeeSky(playerPos)) || !(difficulty = level.getCurrentDifficultyAt(playerPos)).isHarderThan(random.nextFloat() * 3.0f)) continue;
            ServerStatsCounter stats = player.getStats();
            int value = Mth.clamp(stats.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);
            int dayLength = 24000;
            if (random.nextInt(value) < 72000 || !NaturalSpawner.isValidEmptySpawnBlock(level, spawnPos = playerPos.above(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21)), blockState = level.getBlockState(spawnPos), fluidState = level.getFluidState(spawnPos), EntityType.PHANTOM)) continue;
            SpawnGroupData groupData = null;
            int groupSize = 1 + random.nextInt(difficulty.getDifficulty().getId() + 1);
            for (int i = 0; i < groupSize; ++i) {
                Phantom phantom = EntityType.PHANTOM.create(level, EntitySpawnReason.NATURAL);
                if (phantom == null) continue;
                phantom.snapTo(spawnPos, 0.0f, 0.0f);
                groupData = phantom.finalizeSpawn(level, difficulty, EntitySpawnReason.NATURAL, groupData);
                level.addFreshEntityWithPassengers(phantom);
            }
        }
    }
}

