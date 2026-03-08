/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.animal.frog;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.component.DataComponentType;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.EntityDataSerializers;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.tags.BlockTags;
import net.mayaan.tags.EntityTypeTags;
import net.mayaan.tags.ItemTags;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.util.Unit;
import net.mayaan.util.profiling.Profiler;
import net.mayaan.util.profiling.ProfilerFiller;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.AgeableMob;
import net.mayaan.world.entity.AnimationState;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.MoverType;
import net.mayaan.world.entity.Pose;
import net.mayaan.world.entity.SpawnGroupData;
import net.mayaan.world.entity.ai.Brain;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.control.LookControl;
import net.mayaan.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.mayaan.world.entity.ai.navigation.PathNavigation;
import net.mayaan.world.entity.ai.sensing.SensorType;
import net.mayaan.world.entity.animal.Animal;
import net.mayaan.world.entity.animal.frog.FrogAi;
import net.mayaan.world.entity.animal.frog.FrogVariant;
import net.mayaan.world.entity.animal.frog.FrogVariants;
import net.mayaan.world.entity.monster.Slime;
import net.mayaan.world.entity.variant.SpawnContext;
import net.mayaan.world.entity.variant.VariantUtils;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.mayaan.world.level.pathfinder.Node;
import net.mayaan.world.level.pathfinder.PathFinder;
import net.mayaan.world.level.pathfinder.PathType;
import net.mayaan.world.level.pathfinder.PathfindingContext;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Frog
extends Animal {
    private static final Brain.Provider<Frog> BRAIN_PROVIDER = Brain.provider(List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.FROG_ATTACKABLES, SensorType.FROG_TEMPTATIONS, SensorType.IS_IN_WATER), frog -> FrogAi.getActivities());
    private static final EntityDataAccessor<Holder<FrogVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Frog.class, EntityDataSerializers.FROG_VARIANT);
    private static final EntityDataAccessor<OptionalInt> DATA_TONGUE_TARGET_ID = SynchedEntityData.defineId(Frog.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
    private static final int FROG_FALL_DAMAGE_REDUCTION = 5;
    private static final ResourceKey<FrogVariant> DEFAULT_VARIANT = FrogVariants.TEMPERATE;
    public final AnimationState jumpAnimationState = new AnimationState();
    public final AnimationState croakAnimationState = new AnimationState();
    public final AnimationState tongueAnimationState = new AnimationState();
    public final AnimationState swimIdleAnimationState = new AnimationState();

    public Frog(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.lookControl = new FrogLookControl(this, this);
        this.setPathfindingMalus(PathType.WATER, 4.0f);
        this.setPathfindingMalus(PathType.TRAPDOOR, -1.0f);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02f, 0.1f, true);
    }

    protected Brain<Frog> makeBrain(Brain.Packed packedBrain) {
        return BRAIN_PROVIDER.makeBrain(this, packedBrain);
    }

    public Brain<Frog> getBrain() {
        return super.getBrain();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        HolderLookup.RegistryLookup variants = this.registryAccess().lookupOrThrow(Registries.FROG_VARIANT);
        entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(this.registryAccess(), DEFAULT_VARIANT));
        entityData.define(DATA_TONGUE_TARGET_ID, OptionalInt.empty());
    }

    public void eraseTongueTarget() {
        this.entityData.set(DATA_TONGUE_TARGET_ID, OptionalInt.empty());
    }

    public Optional<Entity> getTongueTarget() {
        return this.entityData.get(DATA_TONGUE_TARGET_ID).stream().mapToObj(this.level()::getEntity).filter(Objects::nonNull).findFirst();
    }

    public void setTongueTarget(Entity target) {
        this.entityData.set(DATA_TONGUE_TARGET_ID, OptionalInt.of(target.getId()));
    }

    @Override
    public int getHeadRotSpeed() {
        return 35;
    }

    @Override
    public int getMaxHeadYRot() {
        return 5;
    }

    public Holder<FrogVariant> getVariant() {
        return this.entityData.get(DATA_VARIANT_ID);
    }

    private void setVariant(Holder<FrogVariant> variant) {
        this.entityData.set(DATA_VARIANT_ID, variant);
    }

    @Override
    public <T> @Nullable T get(DataComponentType<? extends T> type) {
        if (type == DataComponents.FROG_VARIANT) {
            return Frog.castComponentValue(type, this.getVariant());
        }
        return super.get(type);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        this.applyImplicitComponentIfPresent(components, DataComponents.FROG_VARIANT);
        super.applyImplicitComponents(components);
    }

    @Override
    protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
        if (type == DataComponents.FROG_VARIANT) {
            this.setVariant(Frog.castComponentValue(DataComponents.FROG_VARIANT, value));
            return true;
        }
        return super.applyImplicitComponent(type, value);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        VariantUtils.writeVariant(output, this.getVariant());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        VariantUtils.readVariant(input, Registries.FROG_VARIANT).ifPresent(this::setVariant);
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("frogBrain");
        this.getBrain().tick(level, this);
        profiler.pop();
        profiler.push("frogActivityUpdate");
        FrogAi.updateActivity(this);
        profiler.pop();
        super.customServerAiStep(level);
    }

    @Override
    public void tick() {
        if (this.level().isClientSide()) {
            this.swimIdleAnimationState.animateWhen(this.isInWater() && !this.walkAnimation.isMoving(), this.tickCount);
        }
        super.tick();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_POSE.equals(accessor)) {
            Pose pose = this.getPose();
            if (pose == Pose.LONG_JUMPING) {
                this.jumpAnimationState.start(this.tickCount);
            } else {
                this.jumpAnimationState.stop();
            }
            if (pose == Pose.CROAKING) {
                this.croakAnimationState.start(this.tickCount);
            } else {
                this.croakAnimationState.stop();
            }
            if (pose == Pose.USING_TONGUE) {
                this.tongueAnimationState.start(this.tickCount);
            } else {
                this.tongueAnimationState.stop();
            }
        }
        super.onSyncedDataUpdated(accessor);
    }

    @Override
    protected void updateWalkAnimation(float distance) {
        float targetSpeed = this.jumpAnimationState.isStarted() ? 0.0f : Math.min(distance * 25.0f, 1.0f);
        this.walkAnimation.update(targetSpeed, 0.4f, this.isBaby() ? 3.0f : 1.0f);
    }

    @Override
    public void playEatingSound() {
        this.level().playSound(null, this, SoundEvents.FROG_EAT, SoundSource.NEUTRAL, 2.0f, 1.0f);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        Frog frog = EntityType.FROG.create(level, EntitySpawnReason.BREEDING);
        if (frog != null) {
            FrogAi.initMemories(frog, level.getRandom());
        }
        return frog;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public void setBaby(boolean baby) {
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel level, Animal partner) {
        this.finalizeSpawnChildFromBreeding(level, partner, null);
        this.getBrain().setMemory(MemoryModuleType.IS_PREGNANT, Unit.INSTANCE);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), Registries.FROG_VARIANT).ifPresent(this::setVariant);
        FrogAi.initMemories(this, level.getRandom());
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MOVEMENT_SPEED, 1.0).add(Attributes.MAX_HEALTH, 10.0).add(Attributes.ATTACK_DAMAGE, 10.0).add(Attributes.STEP_HEIGHT, 1.0);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.FROG_AMBIENT;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.FROG_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.FROG_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        this.playSound(SoundEvents.FROG_STEP, 0.15f, 1.0f);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected int calculateFallDamage(double fallDistance, float damageModifier) {
        return super.calculateFallDamage(fallDistance, damageModifier) - 5;
    }

    @Override
    protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
        this.moveRelative(this.getSpeed(), input);
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
    }

    public static boolean canEat(LivingEntity entity) {
        Slime slime;
        if (entity instanceof Slime && (slime = (Slime)entity).getSize() != 1) {
            return false;
        }
        return entity.is(EntityTypeTags.FROG_FOOD);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FrogPathNavigation(this, level);
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return this.getTargetFromBrain();
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.FROG_FOOD);
    }

    public static boolean checkFrogSpawnRules(EntityType<? extends Animal> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.FROGS_SPAWNABLE_ON) && Frog.isBrightEnoughToSpawn(level, pos);
    }

    private class FrogLookControl
    extends LookControl {
        final /* synthetic */ Frog this$0;

        FrogLookControl(Frog frog, Mob mob) {
            Frog frog2 = frog;
            Objects.requireNonNull(frog2);
            this.this$0 = frog2;
            super(mob);
        }

        @Override
        protected boolean resetXRotOnTick() {
            return this.this$0.getTongueTarget().isEmpty();
        }
    }

    private static class FrogPathNavigation
    extends AmphibiousPathNavigation {
        FrogPathNavigation(Frog mob, Level level) {
            super(mob, level);
        }

        @Override
        public boolean canCutCorner(PathType pathType) {
            return pathType != PathType.WATER_BORDER && super.canCutCorner(pathType);
        }

        @Override
        protected PathFinder createPathFinder(int maxVisitedNodes) {
            this.nodeEvaluator = new FrogNodeEvaluator(true);
            return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
        }
    }

    private static class FrogNodeEvaluator
    extends AmphibiousNodeEvaluator {
        private final BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();

        public FrogNodeEvaluator(boolean prefersShallowSwimming) {
            super(prefersShallowSwimming);
        }

        @Override
        public Node getStart() {
            if (!this.mob.isInWater()) {
                return super.getStart();
            }
            return this.getStartNode(new BlockPos(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().minY), Mth.floor(this.mob.getBoundingBox().minZ)));
        }

        @Override
        public PathType getPathType(PathfindingContext context, int x, int y, int z) {
            this.belowPos.set(x, y - 1, z);
            BlockState belowState = context.getBlockState(this.belowPos);
            if (belowState.is(BlockTags.FROG_PREFER_JUMP_TO)) {
                return PathType.OPEN;
            }
            return super.getPathType(context, x, y, z);
        }
    }
}

