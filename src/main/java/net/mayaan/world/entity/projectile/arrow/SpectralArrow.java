/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.projectile.arrow;

import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.core.particles.SpellParticleOption;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.effect.MobEffects;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.projectile.arrow.AbstractArrow;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class SpectralArrow
extends AbstractArrow {
    private static final int DEFAULT_DURATION = 200;
    private int duration = 200;

    public SpectralArrow(EntityType<? extends SpectralArrow> type, Level level) {
        super((EntityType<? extends AbstractArrow>)type, level);
    }

    public SpectralArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(EntityType.SPECTRAL_ARROW, owner, level, pickupItemStack, firedFromWeapon);
    }

    public SpectralArrow(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(EntityType.SPECTRAL_ARROW, x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide() && !this.isInGround()) {
            this.level().addParticle(SpellParticleOption.create(ParticleTypes.EFFECT, -1, 1.0f), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity mob) {
        super.doPostHurtEffects(mob);
        MobEffectInstance effect = new MobEffectInstance(MobEffects.GLOWING, this.duration, 0);
        mob.addEffect(effect, this.getEffectSource());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.duration = input.getIntOr("Duration", 200);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Duration", this.duration);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }
}

