/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.animal.frog;

import com.google.common.annotations.VisibleForTesting;
import java.util.List;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.EntityDataSerializers;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.tags.ItemTags;
import net.mayaan.util.profiling.Profiler;
import net.mayaan.util.profiling.ProfilerFiller;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.AgeableMob;
import net.mayaan.world.entity.ConversionParams;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.ai.Brain;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.control.SmoothSwimmingLookControl;
import net.mayaan.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.mayaan.world.entity.ai.navigation.PathNavigation;
import net.mayaan.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.mayaan.world.entity.ai.sensing.SensorType;
import net.mayaan.world.entity.animal.Animal;
import net.mayaan.world.entity.animal.Bucketable;
import net.mayaan.world.entity.animal.fish.AbstractFish;
import net.mayaan.world.entity.animal.frog.TadpoleAi;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.component.CustomData;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class Tadpole
extends AbstractFish {
    private static final int DEFAULT_AGE = 0;
    private static final EntityDataAccessor<Boolean> AGE_LOCKED = SynchedEntityData.defineId(Tadpole.class, EntityDataSerializers.BOOLEAN);
    @VisibleForTesting
    public static int ticksToBeFrog = Math.abs(-24000);
    public static final float HITBOX_WIDTH = 0.4f;
    public static final float HITBOX_HEIGHT = 0.3f;
    private int age = 0;
    protected int ageLockParticleTimer = 0;
    private static final Brain.Provider<Tadpole> BRAIN_PROVIDER = Brain.provider(List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.FROG_TEMPTATIONS), tadpole -> TadpoleAi.getActivities());

    public Tadpole(EntityType<? extends AbstractFish> type, Level level) {
        super(type, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02f, 0.1f, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    protected Brain<Tadpole> makeBrain(Brain.Packed packedBrain) {
        return BRAIN_PROVIDER.makeBrain(this, packedBrain);
    }

    public Brain<Tadpole> getBrain() {
        return super.getBrain();
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.TADPOLE_FLOP;
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("tadpoleBrain");
        this.getBrain().tick(level, this);
        profiler.pop();
        profiler.push("tadpoleActivityUpdate");
        TadpoleAi.updateActivity(this);
        profiler.pop();
        super.customServerAiStep(level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MOVEMENT_SPEED, 1.0).add(Attributes.MAX_HEALTH, 6.0);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide() && !this.isAgeLocked()) {
            this.setAge(this.age + 1);
        }
        this.ageLockParticleTimer = AgeableMob.makeAgeLockedParticle(this.level(), this, this.ageLockParticleTimer, this.isAgeLocked());
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Age", this.age);
        output.putBoolean("AgeLocked", this.isAgeLocked());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setAge(input.getIntOr("Age", 0));
        this.setAgeLocked(input.getBooleanOr("AgeLocked", false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(AGE_LOCKED, false);
    }

    protected void setAgeLocked(boolean locked) {
        this.entityData.set(AGE_LOCKED, locked);
    }

    public boolean isAgeLocked() {
        return this.entityData.get(AGE_LOCKED);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.TADPOLE_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.TADPOLE_DEATH;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (this.isFood(itemStack) && !this.isAgeLocked()) {
            this.feed(player, itemStack);
            return InteractionResult.SUCCESS;
        }
        if (AgeableMob.canUseGoldenDandelion(itemStack, true, this.ageLockParticleTimer, this)) {
            AgeableMob.setAgeLocked(this, this::isAgeLocked, player, itemStack, mob -> this.setAgeLockedData());
            return InteractionResult.SUCCESS;
        }
        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    private void setAgeLockedData() {
        this.setAgeLocked(!this.isAgeLocked());
        this.setAge(0);
        this.ageLockParticleTimer = 40;
    }

    @Override
    public boolean fromBucket() {
        return true;
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
    }

    @Override
    public void saveToBucketTag(ItemStack bucket) {
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucket, tag -> {
            tag.putInt("Age", this.getAge());
            tag.putBoolean("AgeLocked", this.isAgeLocked());
        });
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
        tag.getInt("Age").ifPresent(this::setAge);
        this.setAgeLocked(tag.getBooleanOr("AgeLocked", false));
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(Items.TADPOLE_BUCKET);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_TADPOLE;
    }

    private boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.FROG_FOOD);
    }

    private void feed(Player player, ItemStack itemStack) {
        this.usePlayerItem(player, itemStack);
        this.ageUp(AgeableMob.getSpeedUpSecondsWhenFeeding(this.getTicksLeftUntilAdult()));
        this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
    }

    private void usePlayerItem(Player player, ItemStack itemStack) {
        itemStack.consume(1, player);
    }

    private int getAge() {
        return this.age;
    }

    private void ageUp(int ticksToAgeUp) {
        this.setAge(this.age + ticksToAgeUp * 20);
    }

    private void setAge(int newAge) {
        this.age = newAge;
        if (this.age >= ticksToBeFrog) {
            this.ageUp();
        }
    }

    private void ageUp() {
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            this.convertTo(EntityType.FROG, ConversionParams.single(this, false, false), frog -> {
                frog.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(frog.blockPosition()), EntitySpawnReason.CONVERSION, null);
                frog.setPersistenceRequired();
                frog.fudgePositionAfterSizeChange(this.getDimensions(this.getPose()));
                this.playSound(SoundEvents.TADPOLE_GROW_UP, 0.15f, 1.0f);
            });
        }
    }

    private int getTicksLeftUntilAdult() {
        return Math.max(0, ticksToBeFrog - this.age);
    }

    @Override
    public boolean shouldDropExperience() {
        return false;
    }
}

