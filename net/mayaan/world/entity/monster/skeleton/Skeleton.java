/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 */
package net.mayaan.world.entity.monster.skeleton;

import com.google.common.annotations.VisibleForTesting;
import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.EntityDataSerializers;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.ConversionParams;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.monster.skeleton.AbstractSkeleton;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;

public class Skeleton
extends AbstractSkeleton {
    private static final int TOTAL_CONVERSION_TIME = 300;
    private static final EntityDataAccessor<Boolean> DATA_STRAY_CONVERSION_ID = SynchedEntityData.defineId(Skeleton.class, EntityDataSerializers.BOOLEAN);
    public static final String CONVERSION_TAG = "StrayConversionTime";
    private static final int NOT_CONVERTING = -1;
    private int inPowderSnowTime;
    private int conversionTime;

    public Skeleton(EntityType<? extends Skeleton> type, Level level) {
        super((EntityType<? extends AbstractSkeleton>)type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_STRAY_CONVERSION_ID, false);
    }

    public boolean isFreezeConverting() {
        return this.getEntityData().get(DATA_STRAY_CONVERSION_ID);
    }

    public void setFreezeConverting(boolean isConverting) {
        this.entityData.set(DATA_STRAY_CONVERSION_ID, isConverting);
    }

    @Override
    public boolean isShaking() {
        return this.isFreezeConverting();
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide() && this.isAlive() && !this.isNoAi()) {
            if (this.isInPowderSnow) {
                if (this.isFreezeConverting()) {
                    --this.conversionTime;
                    if (this.conversionTime < 0) {
                        this.doFreezeConversion();
                    }
                } else {
                    ++this.inPowderSnowTime;
                    if (this.inPowderSnowTime >= 140) {
                        this.startFreezeConversion(300);
                    }
                }
            } else {
                this.inPowderSnowTime = -1;
                this.setFreezeConverting(false);
            }
        }
        super.tick();
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt(CONVERSION_TAG, this.isFreezeConverting() ? this.conversionTime : -1);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        int conversionTime = input.getIntOr(CONVERSION_TAG, -1);
        if (conversionTime != -1) {
            this.startFreezeConversion(conversionTime);
        } else {
            this.setFreezeConverting(false);
        }
    }

    @VisibleForTesting
    public void startFreezeConversion(int time) {
        this.conversionTime = time;
        this.setFreezeConverting(true);
    }

    protected void doFreezeConversion() {
        this.convertTo(EntityType.STRAY, ConversionParams.single(this, true, true), stray -> {
            if (!this.isSilent()) {
                this.level().levelEvent(null, 1048, this.blockPosition(), 0);
            }
        });
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }
}

