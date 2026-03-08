/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.animal.fish;

import net.mayaan.core.BlockPos;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.EntityDataSerializers;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.Mth;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.EntitySelector;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.MoverType;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.control.MoveControl;
import net.mayaan.world.entity.ai.goal.AvoidEntityGoal;
import net.mayaan.world.entity.ai.goal.PanicGoal;
import net.mayaan.world.entity.ai.goal.RandomSwimmingGoal;
import net.mayaan.world.entity.ai.navigation.PathNavigation;
import net.mayaan.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.mayaan.world.entity.animal.Bucketable;
import net.mayaan.world.entity.animal.fish.WaterAnimal;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import net.mayaan.world.phys.Vec3;

public abstract class AbstractFish
extends WaterAnimal
implements Bucketable {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(AbstractFish.class, EntityDataSerializers.BOOLEAN);
    private static final boolean DEFAULT_FROM_BUCKET = false;

    public AbstractFish(EntityType<? extends AbstractFish> type, Level level) {
        super((EntityType<? extends WaterAnimal>)type, level);
        this.moveControl = new FishMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 8;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(FROM_BUCKET, false);
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setFromBucket(input.getBooleanOr("FromBucket", false));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<Player>(this, Player.class, 8.0f, 1.6, 1.4, EntitySelector.NO_SPECTATORS));
        this.goalSelector.addGoal(4, new FishSwimGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
        this.moveRelative(0.01f, input);
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        if (this.getTarget() == null) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
        }
    }

    @Override
    public void aiStep() {
        if (!this.isInWater() && this.onGround() && this.verticalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0f - 1.0f) * 0.05f, 0.4f, (this.random.nextFloat() * 2.0f - 1.0f) * 0.05f));
            this.setOnGround(false);
            this.needsSync = true;
            this.makeSound(this.getFlopSound());
        }
        super.aiStep();
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    public void saveToBucketTag(ItemStack bucket) {
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    protected boolean canRandomSwim() {
        return true;
    }

    protected abstract SoundEvent getFlopSound();

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
    }

    private static class FishMoveControl
    extends MoveControl {
        private final AbstractFish fish;

        FishMoveControl(AbstractFish fish) {
            super(fish);
            this.fish = fish;
        }

        @Override
        public void tick() {
            if (this.fish.isEyeInFluid(FluidTags.WATER)) {
                this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0, 0.005, 0.0));
            }
            if (this.operation != MoveControl.Operation.MOVE_TO || this.fish.getNavigation().isDone()) {
                this.fish.setSpeed(0.0f);
                return;
            }
            float targetSpeed = (float)(this.speedModifier * this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED));
            this.fish.setSpeed(Mth.lerp(0.125f, this.fish.getSpeed(), targetSpeed));
            double xd = this.wantedX - this.fish.getX();
            double yd = this.wantedY - this.fish.getY();
            double zd = this.wantedZ - this.fish.getZ();
            if (yd != 0.0) {
                double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
                this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0, (double)this.fish.getSpeed() * (yd / dd) * 0.1, 0.0));
            }
            if (xd != 0.0 || zd != 0.0) {
                float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875) - 90.0f;
                this.fish.setYRot(this.rotlerp(this.fish.getYRot(), yRotD, 90.0f));
                this.fish.yBodyRot = this.fish.getYRot();
            }
        }
    }

    private static class FishSwimGoal
    extends RandomSwimmingGoal {
        private final AbstractFish fish;

        public FishSwimGoal(AbstractFish fish) {
            super(fish, 1.0, 40);
            this.fish = fish;
        }

        @Override
        public boolean canUse() {
            return this.fish.canRandomSwim() && super.canUse();
        }
    }
}

