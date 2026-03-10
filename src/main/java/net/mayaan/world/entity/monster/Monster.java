/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.monster;

import java.util.function.Predicate;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.util.RandomSource;
import net.mayaan.world.Difficulty;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.PathfinderMob;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.monster.Enemy;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.ProjectileWeaponItem;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.LightLayer;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.dimension.DimensionType;
import net.mayaan.world.level.gamerules.GameRules;

public abstract class Monster
extends PathfinderMob
implements Enemy {
    protected Monster(EntityType<? extends Monster> type, Level level) {
        super((EntityType<? extends PathfinderMob>)type, level);
        this.xpReward = 5;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    public void aiStep() {
        this.updateSwingTime();
        this.updateNoActionTime();
        super.aiStep();
    }

    protected void updateNoActionTime() {
        float br = this.getLightLevelDependentMagicValue();
        if (br > 0.5f) {
            this.noActionTime += 2;
        }
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.HOSTILE_SWIM;
    }

    @Override
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.HOSTILE_SPLASH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.HOSTILE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HOSTILE_DEATH;
    }

    @Override
    public LivingEntity.Fallsounds getFallSounds() {
        return new LivingEntity.Fallsounds(SoundEvents.HOSTILE_SMALL_FALL, SoundEvents.HOSTILE_BIG_FALL);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return -level.getPathfindingCostFromLightLevels(pos);
    }

    public static boolean isDarkEnoughToSpawn(ServerLevelAccessor level, BlockPos pos, RandomSource random) {
        if (level.getBrightness(LightLayer.SKY, pos) > random.nextInt(32)) {
            return false;
        }
        DimensionType dimensionType = level.dimensionType();
        int blockLightLimit = dimensionType.monsterSpawnBlockLightLimit();
        if (blockLightLimit < 15 && level.getBrightness(LightLayer.BLOCK, pos) > blockLightLimit) {
            return false;
        }
        int brightness = level.getLevel().isThundering() ? level.getMaxLocalRawBrightness(pos, 10) : level.getMaxLocalRawBrightness(pos);
        return brightness <= dimensionType.monsterSpawnLightTest().sample(random);
    }

    public static boolean checkMonsterSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && (EntitySpawnReason.ignoresLightRequirements(spawnReason) || Monster.isDarkEnoughToSpawn(level, pos, random)) && Monster.checkMobSpawnRules(type, level, spawnReason, pos, random);
    }

    public static boolean checkAnyLightMonsterSpawnRules(EntityType<? extends Monster> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && Monster.checkMobSpawnRules(type, level, spawnReason, pos, random);
    }

    public static boolean checkSurfaceMonstersSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return Monster.checkMonsterSpawnRules(type, level, spawnReason, pos, random) && (EntitySpawnReason.isSpawner(spawnReason) || level.canSeeSky(pos));
    }

    public static AttributeSupplier.Builder createMonsterAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
    }

    @Override
    public boolean shouldDropExperience() {
        return true;
    }

    @Override
    protected boolean shouldDropLoot(ServerLevel level) {
        return level.getGameRules().get(GameRules.MOB_DROPS);
    }

    public boolean isPreventingPlayerRest(ServerLevel level, Player player) {
        return true;
    }

    @Override
    public ItemStack getProjectile(ItemStack heldWeapon) {
        if (heldWeapon.getItem() instanceof ProjectileWeaponItem) {
            Predicate<ItemStack> supportedProjectiles = ((ProjectileWeaponItem)heldWeapon.getItem()).getSupportedHeldProjectiles();
            ItemStack heldProjectile = ProjectileWeaponItem.getHeldProjectile(this, supportedProjectiles);
            return heldProjectile.isEmpty() ? new ItemStack(Items.ARROW) : heldProjectile;
        }
        return ItemStack.EMPTY;
    }
}

