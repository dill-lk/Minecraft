/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.animal.chicken;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.Registry;
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
import net.mayaan.tags.ItemTags;
import net.mayaan.util.Mth;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.AgeableMob;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityDimensions;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Pose;
import net.mayaan.world.entity.SpawnGroupData;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.goal.BreedGoal;
import net.mayaan.world.entity.ai.goal.FloatGoal;
import net.mayaan.world.entity.ai.goal.FollowParentGoal;
import net.mayaan.world.entity.ai.goal.LookAtPlayerGoal;
import net.mayaan.world.entity.ai.goal.PanicGoal;
import net.mayaan.world.entity.ai.goal.RandomLookAroundGoal;
import net.mayaan.world.entity.ai.goal.TemptGoal;
import net.mayaan.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.mayaan.world.entity.animal.Animal;
import net.mayaan.world.entity.animal.chicken.ChickenSoundVariant;
import net.mayaan.world.entity.animal.chicken.ChickenSoundVariants;
import net.mayaan.world.entity.animal.chicken.ChickenVariant;
import net.mayaan.world.entity.animal.chicken.ChickenVariants;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.variant.SpawnContext;
import net.mayaan.world.entity.variant.VariantUtils;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.level.pathfinder.PathType;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import net.mayaan.world.level.storage.loot.BuiltInLootTables;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Chicken
extends Animal {
    private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.3f, 0.4f).withEyeHeight(0.28f);
    private static final EntityDataAccessor<Holder<ChickenVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.CHICKEN_VARIANT);
    private static final EntityDataAccessor<Holder<ChickenSoundVariant>> DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.CHICKEN_SOUND_VARIANT);
    private static final boolean DEFAULT_CHICKEN_JOCKEY = false;
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0f;
    private float nextFlap = 1.0f;
    public int eggTime = this.random.nextInt(6000) + 6000;
    public boolean isChickenJockey = false;

    public Chicken(EntityType<? extends Chicken> type, Level level) {
        super((EntityType<? extends Animal>)type, level);
        this.setPathfindingMalus(PathType.WATER, 0.0f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, i -> i.is(ItemTags.CHICKEN_FOOD), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    private Holder<ChickenSoundVariant> getSoundVariant() {
        return this.entityData.get(DATA_SOUND_VARIANT_ID);
    }

    private void setSoundVariant(Holder<ChickenSoundVariant> soundVariant) {
        this.entityData.set(DATA_SOUND_VARIANT_ID, soundVariant);
    }

    private ChickenSoundVariant.ChickenSoundSet getSoundSet() {
        return this.isBaby() ? this.getSoundVariant().value().babySounds() : this.getSoundVariant().value().adultSounds();
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (this.onGround() ? -1.0f : 4.0f) * 0.3f;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0f, 1.0f);
        if (!this.onGround() && this.flapping < 1.0f) {
            this.flapping = 1.0f;
        }
        this.flapping *= 0.9f;
        Vec3 movement = this.getDeltaMovement();
        if (!this.onGround() && movement.y < 0.0) {
            this.setDeltaMovement(movement.multiply(1.0, 0.6, 1.0));
        }
        this.flap += this.flapping * 2.0f;
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel level2 = (ServerLevel)level;
            if (this.isAlive() && !this.isBaby() && !this.isChickenJockey() && --this.eggTime <= 0) {
                if (this.dropFromGiftLootTable(level2, BuiltInLootTables.CHICKEN_LAY, this::spawnAtLocation)) {
                    this.playSound(SoundEvents.CHICKEN_EGG, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
                    this.gameEvent(GameEvent.ENTITY_PLACE);
                }
                this.eggTime = this.random.nextInt(6000) + 6000;
            }
        }
    }

    @Override
    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    @Override
    protected void onFlap() {
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0f;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.getSoundSet().ambientSound().value();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.getSoundSet().hurtSound().value();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.getSoundSet().deathSound().value();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        this.playSound(this.getSoundSet().stepSound().value(), 0.15f, 1.0f);
    }

    @Override
    public @Nullable Chicken getBreedOffspring(ServerLevel level, AgeableMob partner) {
        Chicken baby = EntityType.CHICKEN.create(level, EntitySpawnReason.BREEDING);
        if (baby != null && partner instanceof Chicken) {
            Chicken partnerChicken = (Chicken)partner;
            baby.setVariant(this.random.nextBoolean() ? this.getVariant() : partnerChicken.getVariant());
        }
        return baby;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), Registries.CHICKEN_VARIANT).ifPresent(this::setVariant);
        this.setSoundVariant(ChickenSoundVariants.pickRandomSoundVariant(this.registryAccess(), level.getRandom()));
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.CHICKEN_FOOD);
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        if (this.isChickenJockey()) {
            return 10;
        }
        return super.getBaseExperienceReward(level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        HolderLookup.RegistryLookup chickenSoundVariants = this.registryAccess().lookupOrThrow(Registries.CHICKEN_SOUND_VARIANT);
        entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(this.registryAccess(), ChickenVariants.TEMPERATE));
        entityData.define(DATA_SOUND_VARIANT_ID, (Holder)chickenSoundVariants.get(ChickenSoundVariants.CLASSIC).or(((Registry)chickenSoundVariants)::getAny).orElseThrow());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.isChickenJockey = input.getBooleanOr("IsChickenJockey", false);
        input.getInt("EggLayTime").ifPresent(time -> {
            this.eggTime = time;
        });
        VariantUtils.readVariant(input, Registries.CHICKEN_VARIANT).ifPresent(this::setVariant);
        input.read("sound_variant", ResourceKey.codec(Registries.CHICKEN_SOUND_VARIANT)).flatMap(soundVariant -> this.registryAccess().lookupOrThrow(Registries.CHICKEN_SOUND_VARIANT).get((ResourceKey)soundVariant)).ifPresent(this::setSoundVariant);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("IsChickenJockey", this.isChickenJockey);
        output.putInt("EggLayTime", this.eggTime);
        VariantUtils.writeVariant(output, this.getVariant());
        this.getSoundVariant().unwrapKey().ifPresent(soundVariant -> output.store("sound_variant", ResourceKey.codec(Registries.CHICKEN_SOUND_VARIANT), soundVariant));
    }

    public void setVariant(Holder<ChickenVariant> variant) {
        this.entityData.set(DATA_VARIANT_ID, variant);
    }

    public Holder<ChickenVariant> getVariant() {
        return this.entityData.get(DATA_VARIANT_ID);
    }

    @Override
    public <T> @Nullable T get(DataComponentType<? extends T> type) {
        if (type == DataComponents.CHICKEN_VARIANT) {
            return Chicken.castComponentValue(type, this.getVariant());
        }
        if (type == DataComponents.CHICKEN_SOUND_VARIANT) {
            return Chicken.castComponentValue(type, this.getSoundVariant());
        }
        return super.get(type);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        this.applyImplicitComponentIfPresent(components, DataComponents.CHICKEN_VARIANT);
        super.applyImplicitComponents(components);
    }

    @Override
    protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
        if (type == DataComponents.CHICKEN_VARIANT) {
            this.setVariant(Chicken.castComponentValue(DataComponents.CHICKEN_VARIANT, value));
            return true;
        }
        if (type == DataComponents.CHICKEN_SOUND_VARIANT) {
            this.setSoundVariant(Chicken.castComponentValue(DataComponents.CHICKEN_SOUND_VARIANT, value));
            return true;
        }
        return super.applyImplicitComponent(type, value);
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return this.isChickenJockey();
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        super.positionRider(passenger, moveFunction);
        if (passenger instanceof LivingEntity) {
            ((LivingEntity)passenger).yBodyRot = this.yBodyRot;
        }
    }

    public boolean isChickenJockey() {
        return this.isChickenJockey;
    }

    public void setChickenJockey(boolean isChickenJockey) {
        this.isChickenJockey = isChickenJockey;
    }
}

