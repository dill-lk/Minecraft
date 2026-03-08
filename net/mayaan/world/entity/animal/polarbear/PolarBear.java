/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.animal.polarbear;

import java.util.List;
import java.util.Objects;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.EntityDataSerializers;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.tags.BiomeTags;
import net.mayaan.tags.BlockTags;
import net.mayaan.tags.DamageTypeTags;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.util.TimeUtil;
import net.mayaan.util.valueproviders.UniformInt;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.AgeableMob;
import net.mayaan.world.entity.EntityDimensions;
import net.mayaan.world.entity.EntityReference;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.NeutralMob;
import net.mayaan.world.entity.PathfinderMob;
import net.mayaan.world.entity.Pose;
import net.mayaan.world.entity.SpawnGroupData;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.goal.FloatGoal;
import net.mayaan.world.entity.ai.goal.FollowParentGoal;
import net.mayaan.world.entity.ai.goal.LookAtPlayerGoal;
import net.mayaan.world.entity.ai.goal.MeleeAttackGoal;
import net.mayaan.world.entity.ai.goal.PanicGoal;
import net.mayaan.world.entity.ai.goal.RandomLookAroundGoal;
import net.mayaan.world.entity.ai.goal.RandomStrollGoal;
import net.mayaan.world.entity.ai.goal.target.HurtByTargetGoal;
import net.mayaan.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.mayaan.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.mayaan.world.entity.animal.Animal;
import net.mayaan.world.entity.animal.fox.Fox;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class PolarBear
extends Animal
implements NeutralMob {
    private static final EntityDataAccessor<Boolean> DATA_STANDING_ID = SynchedEntityData.defineId(PolarBear.class, EntityDataSerializers.BOOLEAN);
    private static final float STAND_ANIMATION_TICKS = 6.0f;
    private float clientSideStandAnimationO;
    private float clientSideStandAnimation;
    private int warningSoundTicks;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private long persistentAngerEndTime;
    private @Nullable EntityReference<LivingEntity> persistentAngerTarget;

    public PolarBear(EntityType<? extends PolarBear> type, Level level) {
        super((EntityType<? extends Animal>)type, level);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return EntityType.POLAR_BEAR.create(level, EntitySpawnReason.BREEDING);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PolarBearMeleeAttackGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal((PathfinderMob)this, 2.0, bear -> bear.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new PolarBearHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new PolarBearAttackPlayersGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<Player>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<Fox>(this, Fox.class, 10, true, true, (target, level) -> !this.isBaby()));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<PolarBear>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 6.0);
    }

    public static boolean checkPolarBearSpawnRules(EntityType<PolarBear> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        Holder<Biome> biome = level.getBiome(pos);
        if (biome.is(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS)) {
            return PolarBear.isBrightEnoughToSpawn(level, pos) && level.getBlockState(pos.below()).is(BlockTags.POLAR_BEARS_SPAWNABLE_ON_ALTERNATE);
        }
        return PolarBear.checkAnimalSpawnRules(type, level, spawnReason, pos, random);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.readPersistentAngerSaveData(this.level(), input);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.addPersistentAngerSaveData(output);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setPersistentAngerEndTime(long endTime) {
        this.persistentAngerEndTime = endTime;
    }

    @Override
    public long getPersistentAngerEndTime() {
        return this.persistentAngerEndTime;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isBaby()) {
            return SoundEvents.POLAR_BEAR_AMBIENT_BABY;
        }
        return SoundEvents.POLAR_BEAR_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.POLAR_BEAR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.POLAR_BEAR_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15f, 1.0f);
    }

    protected void playWarningSound() {
        if (this.warningSoundTicks <= 0) {
            this.makeSound(SoundEvents.POLAR_BEAR_WARNING);
            this.warningSoundTicks = 40;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_STANDING_ID, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.clientSideStandAnimation != this.clientSideStandAnimationO) {
                this.refreshDimensions();
            }
            this.clientSideStandAnimationO = this.clientSideStandAnimation;
            this.clientSideStandAnimation = this.isStanding() ? Mth.clamp(this.clientSideStandAnimation + 1.0f, 0.0f, 6.0f) : Mth.clamp(this.clientSideStandAnimation - 1.0f, 0.0f, 6.0f);
        }
        if (this.warningSoundTicks > 0) {
            --this.warningSoundTicks;
        }
        if (!this.level().isClientSide()) {
            this.updatePersistentAnger((ServerLevel)this.level(), true);
        }
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        if (this.clientSideStandAnimation > 0.0f) {
            float standFactor = this.clientSideStandAnimation / 6.0f;
            float heightScaleFactor = 1.0f + standFactor;
            return super.getDefaultDimensions(pose).scale(1.0f, heightScaleFactor);
        }
        return super.getDefaultDimensions(pose);
    }

    public boolean isStanding() {
        return this.entityData.get(DATA_STANDING_ID);
    }

    public void setStanding(boolean value) {
        this.entityData.set(DATA_STANDING_ID, value);
    }

    public float getStandingAnimationScale(float a) {
        return Mth.lerp(a, this.clientSideStandAnimationO, this.clientSideStandAnimation) / 6.0f;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98f;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        if (groupData == null) {
            groupData = new AgeableMob.AgeableMobGroupData(1.0f);
        }
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    private class PolarBearMeleeAttackGoal
    extends MeleeAttackGoal {
        final /* synthetic */ PolarBear this$0;

        public PolarBearMeleeAttackGoal(PolarBear polarBear) {
            PolarBear polarBear2 = polarBear;
            Objects.requireNonNull(polarBear2);
            this.this$0 = polarBear2;
            super(polarBear, 1.25, true);
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target)) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(PolarBearMeleeAttackGoal.getServerLevel(this.mob), target);
                this.this$0.setStanding(false);
            } else if (this.mob.distanceToSqr(target) < (double)((target.getBbWidth() + 3.0f) * (target.getBbWidth() + 3.0f))) {
                if (this.isTimeToAttack()) {
                    this.this$0.setStanding(false);
                    this.resetAttackCooldown();
                }
                if (this.getTicksUntilNextAttack() <= 10) {
                    this.this$0.setStanding(true);
                    this.this$0.playWarningSound();
                }
            } else {
                this.resetAttackCooldown();
                this.this$0.setStanding(false);
            }
        }

        @Override
        public void stop() {
            this.this$0.setStanding(false);
            super.stop();
        }
    }

    private class PolarBearHurtByTargetGoal
    extends HurtByTargetGoal {
        final /* synthetic */ PolarBear this$0;

        public PolarBearHurtByTargetGoal(PolarBear polarBear) {
            PolarBear polarBear2 = polarBear;
            Objects.requireNonNull(polarBear2);
            this.this$0 = polarBear2;
            super(polarBear, new Class[0]);
        }

        @Override
        public void start() {
            super.start();
            if (this.this$0.isBaby()) {
                this.alertOthers();
                this.stop();
            }
        }

        @Override
        protected void alertOther(Mob other, LivingEntity hurtByMob) {
            if (other instanceof PolarBear && !other.isBaby()) {
                super.alertOther(other, hurtByMob);
            }
        }
    }

    private class PolarBearAttackPlayersGoal
    extends NearestAttackableTargetGoal<Player> {
        final /* synthetic */ PolarBear this$0;

        public PolarBearAttackPlayersGoal(PolarBear polarBear) {
            PolarBear polarBear2 = polarBear;
            Objects.requireNonNull(polarBear2);
            this.this$0 = polarBear2;
            super(polarBear, Player.class, 20, true, true, null);
        }

        @Override
        public boolean canUse() {
            if (this.this$0.isBaby()) {
                return false;
            }
            if (super.canUse()) {
                List<PolarBear> bears = this.this$0.level().getEntitiesOfClass(PolarBear.class, this.this$0.getBoundingBox().inflate(8.0, 4.0, 8.0));
                for (PolarBear bear : bears) {
                    if (!bear.isBaby()) continue;
                    return true;
                }
            }
            return false;
        }

        @Override
        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5;
        }
    }
}

