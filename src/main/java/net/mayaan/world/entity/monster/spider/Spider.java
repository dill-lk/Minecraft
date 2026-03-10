/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.monster.spider;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.EntityDataSerializers;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.util.RandomSource;
import net.mayaan.world.Difficulty;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.effect.MobEffects;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.SpawnGroupData;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.goal.AvoidEntityGoal;
import net.mayaan.world.entity.ai.goal.FloatGoal;
import net.mayaan.world.entity.ai.goal.LeapAtTargetGoal;
import net.mayaan.world.entity.ai.goal.LookAtPlayerGoal;
import net.mayaan.world.entity.ai.goal.MeleeAttackGoal;
import net.mayaan.world.entity.ai.goal.RandomLookAroundGoal;
import net.mayaan.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.mayaan.world.entity.ai.goal.target.HurtByTargetGoal;
import net.mayaan.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.mayaan.world.entity.ai.navigation.PathNavigation;
import net.mayaan.world.entity.ai.navigation.WallClimberNavigation;
import net.mayaan.world.entity.animal.armadillo.Armadillo;
import net.mayaan.world.entity.animal.golem.IronGolem;
import net.mayaan.world.entity.monster.Monster;
import net.mayaan.world.entity.monster.skeleton.Skeleton;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Spider
extends Monster {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Spider.class, EntityDataSerializers.BYTE);
    private static final float SPIDER_SPECIAL_EFFECT_CHANCE = 0.1f;

    public Spider(EntityType<? extends Spider> type, Level level) {
        super((EntityType<? extends Monster>)type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<Armadillo>(this, Armadillo.class, 6.0f, 1.0, 1.2, entity -> !((Armadillo)entity).isScared()));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4f));
        this.goalSelector.addGoal(4, new SpiderAttackGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new SpiderTargetGoal<Player>(this, Player.class));
        this.targetSelector.addGoal(3, new SpiderTargetGoal<IronGolem>(this, IronGolem.class));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            this.setClimbing(this.horizontalCollision);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.3f);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15f, 1.0f);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vec3 speedMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, speedMultiplier);
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance newEffect) {
        if (newEffect.is(MobEffects.POISON)) {
            return false;
        }
        return super.canBeAffected(newEffect);
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean value) {
        byte flags = this.entityData.get(DATA_FLAGS_ID);
        flags = value ? (byte)(flags | 1) : (byte)(flags & 0xFFFFFFFE);
        this.entityData.set(DATA_FLAGS_ID, flags);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        Skeleton skeleton;
        groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
        RandomSource random = level.getRandom();
        if (random.nextInt(100) == 0 && (skeleton = EntityType.SKELETON.create(this.level(), EntitySpawnReason.JOCKEY)) != null) {
            skeleton.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0f);
            skeleton.finalizeSpawn(level, difficulty, spawnReason, null);
            skeleton.startRiding(this, false, false);
        }
        if (groupData == null) {
            groupData = new SpiderEffectsGroupData();
            if (level.getDifficulty() == Difficulty.HARD && random.nextFloat() < 0.1f * difficulty.getSpecialMultiplier()) {
                ((SpiderEffectsGroupData)groupData).setRandomEffect(random);
            }
        }
        if (groupData instanceof SpiderEffectsGroupData) {
            SpiderEffectsGroupData spiderEffectsGroupData = (SpiderEffectsGroupData)groupData;
            Holder<MobEffect> effect = spiderEffectsGroupData.effect;
            if (effect != null) {
                this.addEffect(new MobEffectInstance(effect, -1));
            }
        }
        return groupData;
    }

    @Override
    public Vec3 getVehicleAttachmentPoint(Entity vehicle) {
        if (vehicle.getBbWidth() <= this.getBbWidth()) {
            return new Vec3(0.0, 0.3125 * (double)this.getScale(), 0.0);
        }
        return super.getVehicleAttachmentPoint(vehicle);
    }

    private static class SpiderAttackGoal
    extends MeleeAttackGoal {
        public SpiderAttackGoal(Spider mob) {
            super(mob, 1.0, true);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }

        @Override
        public boolean canContinueToUse() {
            float br = this.mob.getLightLevelDependentMagicValue();
            if (br >= 0.5f && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
                return false;
            }
            return super.canContinueToUse();
        }
    }

    private static class SpiderTargetGoal<T extends LivingEntity>
    extends NearestAttackableTargetGoal<T> {
        public SpiderTargetGoal(Spider mob, Class<T> targetType) {
            super((Mob)mob, targetType, true);
        }

        @Override
        public boolean canUse() {
            float br = this.mob.getLightLevelDependentMagicValue();
            if (br >= 0.5f) {
                return false;
            }
            return super.canUse();
        }
    }

    public static class SpiderEffectsGroupData
    implements SpawnGroupData {
        public @Nullable Holder<MobEffect> effect;

        public void setRandomEffect(RandomSource random) {
            int selection = random.nextInt(5);
            if (selection <= 1) {
                this.effect = MobEffects.SPEED;
            } else if (selection <= 2) {
                this.effect = MobEffects.STRENGTH;
            } else if (selection <= 3) {
                this.effect = MobEffects.REGENERATION;
            } else if (selection <= 4) {
                this.effect = MobEffects.INVISIBILITY;
            }
        }
    }
}

