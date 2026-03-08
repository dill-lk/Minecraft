/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.animal.fish;

import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.PathfinderMob;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.pathfinder.PathType;

public abstract class WaterAnimal
extends PathfinderMob {
    public static final int AMBIENT_SOUND_INTERVAL = 120;

    protected WaterAnimal(EntityType<? extends WaterAnimal> type, Level level) {
        super((EntityType<? extends PathfinderMob>)type, level);
        this.setPathfindingMalus(PathType.WATER, 0.0f);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 1 + this.random.nextInt(3);
    }

    protected void handleAirSupply(ServerLevel level, int preTickAirSupply) {
        if (this.isAlive() && !this.isInWater()) {
            this.setAirSupply(preTickAirSupply - 1);
            if (this.shouldTakeDrowningDamage()) {
                this.setAirSupply(0);
                this.hurtServer(level, this.damageSources().drown(), 2.0f);
            }
        } else {
            this.setAirSupply(300);
        }
    }

    @Override
    public void baseTick() {
        int airSupply = this.getAirSupply();
        super.baseTick();
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            this.handleAirSupply(serverLevel, airSupply);
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<? extends WaterAnimal> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        int seaLevel = level.getSeaLevel();
        int minSpawnLevel = seaLevel - 13;
        return pos.getY() >= minSpawnLevel && pos.getY() <= seaLevel && level.getFluidState(pos.below()).is(FluidTags.WATER) && level.getBlockState(pos.above()).is(Blocks.WATER);
    }
}

