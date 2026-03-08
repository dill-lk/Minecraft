/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.monster.skeleton;

import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.effect.MobEffects;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.monster.skeleton.AbstractSkeleton;
import net.mayaan.world.entity.projectile.arrow.AbstractArrow;
import net.mayaan.world.entity.projectile.arrow.Arrow;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import org.jspecify.annotations.Nullable;

public class Parched
extends AbstractSkeleton {
    public Parched(EntityType<? extends AbstractSkeleton> type, Level level) {
        super(type, level);
    }

    @Override
    protected AbstractArrow getArrow(ItemStack projectile, float power, @Nullable ItemStack firingWeapon) {
        AbstractArrow arrow = super.getArrow(projectile, power, firingWeapon);
        if (arrow instanceof Arrow) {
            ((Arrow)arrow).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600));
        }
        return arrow;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes().add(Attributes.MAX_HEALTH, 16.0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PARCHED_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PARCHED_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARCHED_DEATH;
    }

    @Override
    SoundEvent getStepSound() {
        return SoundEvents.PARCHED_STEP;
    }

    @Override
    protected int getHardAttackInterval() {
        return 50;
    }

    @Override
    protected int getAttackInterval() {
        return 70;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance newEffect) {
        if (newEffect.getEffect() == MobEffects.WEAKNESS) {
            return false;
        }
        return super.canBeAffected(newEffect);
    }
}

