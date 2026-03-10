/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.animal.fish;

import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.animal.fish.AbstractSchoolingFish;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;

public class Cod
extends AbstractSchoolingFish {
    public Cod(EntityType<? extends Cod> type, Level level) {
        super((EntityType<? extends AbstractSchoolingFish>)type, level);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(Items.COD_BUCKET);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COD_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.COD_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }
}

