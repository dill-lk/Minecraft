/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.animal.equine;

import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.AgeableMob;
import net.mayaan.world.entity.EntityAttachment;
import net.mayaan.world.entity.EntityAttachments;
import net.mayaan.world.entity.EntityDimensions;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.Pose;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.animal.Animal;
import net.mayaan.world.entity.animal.equine.AbstractHorse;
import net.mayaan.world.entity.animal.equine.SkeletonTrapGoal;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class SkeletonHorse
extends AbstractHorse {
    private final SkeletonTrapGoal skeletonTrapGoal = new SkeletonTrapGoal(this);
    private static final int TRAP_MAX_LIFE = 18000;
    private static final boolean DEFAULT_IS_TRAP = false;
    private static final int DEFAULT_TRAP_TIME = 0;
    private static final EntityDimensions BABY_DIMENSIONS = EntityType.SKELETON_HORSE.getDimensions().withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0f, EntityType.SKELETON_HORSE.getHeight() - 0.25f, 0.0f)).scale(0.7f);
    private boolean isTrap = false;
    private int trapTime = 0;

    public SkeletonHorse(EntityType<? extends SkeletonHorse> type, Level level) {
        super((EntityType<? extends AbstractHorse>)type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return SkeletonHorse.createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.MOVEMENT_SPEED, 0.2f);
    }

    public static boolean checkSkeletonHorseSpawnRules(EntityType<? extends Animal> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        if (EntitySpawnReason.isSpawner(spawnReason)) {
            return EntitySpawnReason.ignoresLightRequirements(spawnReason) || SkeletonHorse.isBrightEnoughToSpawn(level, pos);
        }
        return Animal.checkAnimalSpawnRules(type, level, spawnReason, pos, random);
    }

    @Override
    protected void randomizeAttributes(RandomSource random) {
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(SkeletonHorse.generateJumpStrength(random::nextDouble));
    }

    @Override
    protected void addBehaviourGoals() {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isEyeInFluid(FluidTags.WATER)) {
            return SoundEvents.SKELETON_HORSE_AMBIENT_WATER;
        }
        return SoundEvents.SKELETON_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SKELETON_HORSE_HURT;
    }

    @Override
    protected SoundEvent getSwimSound() {
        if (this.onGround()) {
            if (this.isVehicle()) {
                ++this.gallopSoundCounter;
                if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                    return SoundEvents.SKELETON_HORSE_GALLOP_WATER;
                }
                if (this.gallopSoundCounter <= 5) {
                    return SoundEvents.SKELETON_HORSE_STEP_WATER;
                }
            } else {
                return SoundEvents.SKELETON_HORSE_STEP_WATER;
            }
        }
        return SoundEvents.SKELETON_HORSE_SWIM;
    }

    @Override
    protected void playSwimSound(float volume) {
        if (this.onGround()) {
            super.playSwimSound(0.3f);
        } else {
            super.playSwimSound(Math.min(0.1f, volume * 25.0f));
        }
    }

    @Override
    protected void playJumpSound() {
        if (this.isInWater()) {
            this.playSound(SoundEvents.SKELETON_HORSE_JUMP_WATER, 0.4f, 1.0f);
        } else {
            super.playJumpSound();
        }
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isTrap() && this.trapTime++ >= 18000) {
            this.discard();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("SkeletonTrap", this.isTrap());
        output.putInt("SkeletonTrapTime", this.trapTime);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setTrap(input.getBooleanOr("SkeletonTrap", false));
        this.trapTime = input.getIntOr("SkeletonTrapTime", 0);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.96f;
    }

    public boolean isTrap() {
        return this.isTrap;
    }

    public void setTrap(boolean trap) {
        if (trap == this.isTrap) {
            return;
        }
        this.isTrap = trap;
        if (trap) {
            this.goalSelector.addGoal(1, this.skeletonTrapGoal);
        } else {
            this.goalSelector.removeGoal(this.skeletonTrapGoal);
        }
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return EntityType.SKELETON_HORSE.create(level, EntitySpawnReason.BREEDING);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.isTamed()) {
            return InteractionResult.PASS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean canUseSlot(EquipmentSlot slot) {
        return true;
    }

    @Override
    public boolean canAgeUp() {
        return false;
    }
}

