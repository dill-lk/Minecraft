/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.animal.nautilus;

import java.util.List;
import net.mayaan.core.Holder;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.component.DataComponentType;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.EntityDataSerializers;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.util.profiling.Profiler;
import net.mayaan.util.profiling.ProfilerFiller;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.AgeableMob;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.SpawnGroupData;
import net.mayaan.world.entity.ai.Brain;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.sensing.SensorType;
import net.mayaan.world.entity.animal.nautilus.AbstractNautilus;
import net.mayaan.world.entity.animal.nautilus.ZombieNautilusAi;
import net.mayaan.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.mayaan.world.entity.animal.nautilus.ZombieNautilusVariants;
import net.mayaan.world.entity.variant.SpawnContext;
import net.mayaan.world.entity.variant.VariantUtils;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class ZombieNautilus
extends AbstractNautilus {
    private static final Brain.Provider<ZombieNautilus> BRAIN_PROVIDER = Brain.provider(List.of(MemoryModuleType.ANGRY_AT, MemoryModuleType.ATTACK_TARGET_COOLDOWN), List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NAUTILUS_TEMPTATIONS), zombieNautilus -> ZombieNautilusAi.getActivities());
    private static final EntityDataAccessor<Holder<ZombieNautilusVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(ZombieNautilus.class, EntityDataSerializers.ZOMBIE_NAUTILUS_VARIANT);

    public ZombieNautilus(EntityType<? extends ZombieNautilus> type, Level level) {
        super((EntityType<? extends AbstractNautilus>)type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractNautilus.createAttributes().add(Attributes.MOVEMENT_SPEED, 1.1f);
    }

    @Override
    public @Nullable ZombieNautilus getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }

    @Override
    protected EquipmentSlot sunProtectionSlot() {
        return EquipmentSlot.BODY;
    }

    protected Brain<ZombieNautilus> makeBrain(Brain.Packed packedBrain) {
        return BRAIN_PROVIDER.makeBrain(this, packedBrain);
    }

    public Brain<ZombieNautilus> getBrain() {
        return super.getBrain();
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("zombieNautilusBrain");
        this.getBrain().tick(level, this);
        profiler.pop();
        profiler.push("zombieNautilusActivityUpdate");
        ZombieNautilusAi.updateActivity(this);
        profiler.pop();
        super.customServerAiStep(level);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_AMBIENT : SoundEvents.ZOMBIE_NAUTILUS_AMBIENT_ON_LAND;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_HURT : SoundEvents.ZOMBIE_NAUTILUS_HURT_ON_LAND;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_DEATH : SoundEvents.ZOMBIE_NAUTILUS_DEATH_ON_LAND;
    }

    @Override
    protected SoundEvent getDashSound() {
        return this.isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_DASH : SoundEvents.ZOMBIE_NAUTILUS_DASH_ON_LAND;
    }

    @Override
    protected SoundEvent getDashReadySound() {
        return this.isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_DASH_READY : SoundEvents.ZOMBIE_NAUTILUS_DASH_READY_ON_LAND;
    }

    @Override
    protected void playEatingSound() {
        this.makeSound(SoundEvents.ZOMBIE_NAUTILUS_EAT);
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.ZOMBIE_NAUTILUS_SWIM;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(this.registryAccess(), ZombieNautilusVariants.TEMPERATE));
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        VariantUtils.readVariant(input, Registries.ZOMBIE_NAUTILUS_VARIANT).ifPresent(this::setVariant);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        VariantUtils.writeVariant(output, this.getVariant());
    }

    public void setVariant(Holder<ZombieNautilusVariant> variant) {
        this.entityData.set(DATA_VARIANT_ID, variant);
    }

    public Holder<ZombieNautilusVariant> getVariant() {
        return this.entityData.get(DATA_VARIANT_ID);
    }

    @Override
    public <T> @Nullable T get(DataComponentType<? extends T> type) {
        if (type == DataComponents.ZOMBIE_NAUTILUS_VARIANT) {
            return ZombieNautilus.castComponentValue(type, this.getVariant());
        }
        return super.get(type);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        this.applyImplicitComponentIfPresent(components, DataComponents.ZOMBIE_NAUTILUS_VARIANT);
        super.applyImplicitComponents(components);
    }

    @Override
    protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
        if (type == DataComponents.ZOMBIE_NAUTILUS_VARIANT) {
            Holder<ZombieNautilusVariant> variant = ZombieNautilus.castComponentValue(DataComponents.ZOMBIE_NAUTILUS_VARIANT, value);
            this.setVariant(variant);
            return true;
        }
        return super.applyImplicitComponent(type, value);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), Registries.ZOMBIE_NAUTILUS_VARIANT).ifPresent(this::setVariant);
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    @Override
    public boolean canBeLeashed() {
        return !this.isAggravated() && !this.isMobControlled();
    }

    @Override
    public boolean isBaby() {
        return false;
    }
}

