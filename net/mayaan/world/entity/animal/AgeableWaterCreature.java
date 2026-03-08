/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.animal;

import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.AgeableMob;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.pathfinder.PathType;

public abstract class AgeableWaterCreature
extends AgeableMob {
    protected AgeableWaterCreature(EntityType<? extends AgeableWaterCreature> type, Level level) {
        super((EntityType<? extends AgeableMob>)type, level);
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
    public int getBaseExperienceReward(ServerLevel level) {
        return 1 + this.random.nextInt(3);
    }

    protected void handleAirSupply(int preTickAirSupply) {
        if (this.isAlive() && !this.isInWater()) {
            this.setAirSupply(preTickAirSupply - 1);
            if (this.shouldTakeDrowningDamage()) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().drown(), 2.0f);
            }
        } else {
            this.setAirSupply(300);
        }
    }

    @Override
    public void baseTick() {
        int airSupply = this.getAirSupply();
        super.baseTick();
        this.handleAirSupply(airSupply);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    public static boolean checkSurfaceAgeableWaterCreatureSpawnRules(EntityType<? extends AgeableWaterCreature> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        int seaLevel = level.getSeaLevel();
        int minSpawnLevel = seaLevel - 13;
        return pos.getY() >= minSpawnLevel && pos.getY() <= seaLevel && level.getFluidState(pos.below()).is(FluidTags.WATER) && level.getBlockState(pos.above()).is(Blocks.WATER);
    }
}

