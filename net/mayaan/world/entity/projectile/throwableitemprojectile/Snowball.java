/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.projectile.throwableitemprojectile;

import net.mayaan.core.particles.ItemParticleOption;
import net.mayaan.core.particles.ParticleOptions;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.monster.Blaze;
import net.mayaan.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ItemStackTemplate;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.phys.EntityHitResult;
import net.mayaan.world.phys.HitResult;

public class Snowball
extends ThrowableItemProjectile {
    public Snowball(EntityType<? extends Snowball> type, Level level) {
        super((EntityType<? extends ThrowableItemProjectile>)type, level);
    }

    public Snowball(Level level, LivingEntity mob, ItemStack itemStack) {
        super(EntityType.SNOWBALL, mob, level, itemStack);
    }

    public Snowball(Level level, double x, double y, double z, ItemStack itemStack) {
        super(EntityType.SNOWBALL, x, y, z, level, itemStack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    private ParticleOptions getParticle() {
        ItemStack item = this.getItem();
        return item.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(item));
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particle = this.getParticle();
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity entity = hitResult.getEntity();
        int damage = entity instanceof Blaze ? 3 : 0;
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), damage);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }
}

