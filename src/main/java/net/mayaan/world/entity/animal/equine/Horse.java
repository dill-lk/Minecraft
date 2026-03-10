/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.animal.equine;

import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.component.DataComponentType;
import net.mayaan.core.component.DataComponents;
import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.EntityDataSerializers;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.util.RandomSource;
import net.mayaan.util.Util;
import net.mayaan.world.DifficultyInstance;
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
import net.mayaan.world.entity.SpawnGroupData;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.animal.Animal;
import net.mayaan.world.entity.animal.equine.AbstractHorse;
import net.mayaan.world.entity.animal.equine.Donkey;
import net.mayaan.world.entity.animal.equine.Markings;
import net.mayaan.world.entity.animal.equine.Mule;
import net.mayaan.world.entity.animal.equine.Variant;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.block.SoundType;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class Horse
extends AbstractHorse {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(Horse.class, EntityDataSerializers.INT);
    private static final EntityDimensions BABY_DIMENSIONS = EntityType.HORSE.getDimensions().withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0f, EntityType.HORSE.getHeight() - 0.125f, 0.0f)).scale(0.7f);
    private static final int DEFAULT_VARIANT = 0;

    public Horse(EntityType<? extends Horse> type, Level level) {
        super((EntityType<? extends AbstractHorse>)type, level);
    }

    @Override
    protected void randomizeAttributes(RandomSource random) {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Horse.generateMaxHealth(random::nextInt));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Horse.generateSpeed(random::nextDouble));
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(Horse.generateJumpStrength(random::nextDouble));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_ID_TYPE_VARIANT, 0);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Variant", this.getTypeVariant());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setTypeVariant(input.getIntOr("Variant", 0));
    }

    private void setTypeVariant(int i) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, i);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariantAndMarkings(Variant variant, Markings markings) {
        this.setTypeVariant(variant.getId() & 0xFF | markings.getId() << 8 & 0xFF00);
    }

    public Variant getVariant() {
        return Variant.byId(this.getTypeVariant() & 0xFF);
    }

    private void setVariant(Variant variant) {
        this.setTypeVariant(variant.getId() & 0xFF | this.getTypeVariant() & 0xFFFFFF00);
    }

    @Override
    public <T> @Nullable T get(DataComponentType<? extends T> type) {
        if (type == DataComponents.HORSE_VARIANT) {
            return Horse.castComponentValue(type, this.getVariant());
        }
        return super.get(type);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        this.applyImplicitComponentIfPresent(components, DataComponents.HORSE_VARIANT);
        super.applyImplicitComponents(components);
    }

    @Override
    protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
        if (type == DataComponents.HORSE_VARIANT) {
            this.setVariant(Horse.castComponentValue(DataComponents.HORSE_VARIANT, value));
            return true;
        }
        return super.applyImplicitComponent(type, value);
    }

    public Markings getMarkings() {
        return Markings.byId((this.getTypeVariant() & 0xFF00) >> 8);
    }

    @Override
    protected void playGallopSound(SoundType soundType) {
        super.playGallopSound(soundType);
        if (this.random.nextInt(10) == 0) {
            this.playSound(this.isBaby() ? SoundEvents.HORSE_BREATHE_BABY : SoundEvents.HORSE_BREATHE, soundType.getVolume() * 0.6f, soundType.getPitch());
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? SoundEvents.HORSE_AMBIENT_BABY : SoundEvents.HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? SoundEvents.HORSE_DEATH_BABY : SoundEvents.HORSE_DEATH;
    }

    @Override
    protected SoundEvent getEatingSound() {
        return this.isBaby() ? SoundEvents.HORSE_EAT_BABY : SoundEvents.HORSE_EAT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isBaby() ? SoundEvents.HORSE_HURT_BABY : SoundEvents.HORSE_HURT;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return this.isBaby() ? SoundEvents.HORSE_ANGRY_BABY : SoundEvents.HORSE_ANGRY;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean shouldOpenInventory;
        boolean bl = shouldOpenInventory = !this.isBaby() && this.isTamed() && player.isSecondaryUseActive();
        if (this.isVehicle() || shouldOpenInventory || this.isBaby() && player.isHolding(Items.GOLDEN_DANDELION)) {
            return super.mobInteract(player, hand);
        }
        ItemStack itemStack = player.getItemInHand(hand);
        if (!itemStack.isEmpty()) {
            if (this.isFood(itemStack)) {
                return this.fedFood(player, itemStack);
            }
            if (!this.isTamed()) {
                this.makeMad();
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean canMate(Animal partner) {
        if (partner == this) {
            return false;
        }
        if (partner instanceof Donkey || partner instanceof Horse) {
            return this.canParent() && ((AbstractHorse)partner).canParent();
        }
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        if (partner instanceof Donkey) {
            Mule baby = EntityType.MULE.create(level, EntitySpawnReason.BREEDING);
            if (baby != null) {
                this.setOffspringAttributes(partner, baby);
            }
            return baby;
        }
        Horse horsePartner = (Horse)partner;
        Horse baby = EntityType.HORSE.create(level, EntitySpawnReason.BREEDING);
        if (baby != null) {
            int selectSkin = this.random.nextInt(9);
            Variant variant = selectSkin < 4 ? this.getVariant() : (selectSkin < 8 ? horsePartner.getVariant() : Util.getRandom(Variant.values(), this.random));
            int selectMarking = this.random.nextInt(5);
            Markings markings = selectMarking < 2 ? this.getMarkings() : (selectMarking < 4 ? horsePartner.getMarkings() : Util.getRandom(Markings.values(), this.random));
            baby.setVariantAndMarkings(variant, markings);
            this.setOffspringAttributes(partner, baby);
        }
        return baby;
    }

    @Override
    public boolean canUseSlot(EquipmentSlot slot) {
        return true;
    }

    @Override
    protected void hurtArmor(DamageSource damageSource, float damage) {
        this.doHurtEquipment(damageSource, damage, EquipmentSlot.BODY);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        Variant variant;
        RandomSource random = level.getRandom();
        if (groupData instanceof HorseGroupData) {
            variant = ((HorseGroupData)groupData).variant;
        } else {
            variant = Util.getRandom(Variant.values(), random);
            groupData = new HorseGroupData(variant);
        }
        this.setVariantAndMarkings(variant, Util.getRandom(Markings.values(), random));
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    public static class HorseGroupData
    extends AgeableMob.AgeableMobGroupData {
        public final Variant variant;

        public HorseGroupData(Variant variant) {
            super(true);
            this.variant = variant;
        }
    }
}

