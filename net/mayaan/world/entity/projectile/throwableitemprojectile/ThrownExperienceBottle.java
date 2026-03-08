/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.projectile.throwableitemprojectile;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.ExperienceOrb;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.HitResult;
import net.mayaan.world.phys.Vec3;

public class ThrownExperienceBottle
extends ThrowableItemProjectile {
    public ThrownExperienceBottle(EntityType<? extends ThrownExperienceBottle> type, Level level) {
        super((EntityType<? extends ThrowableItemProjectile>)type, level);
    }

    public ThrownExperienceBottle(Level level, LivingEntity mob, ItemStack itemStack) {
        super(EntityType.EXPERIENCE_BOTTLE, mob, level, itemStack);
    }

    public ThrownExperienceBottle(Level level, double x, double y, double z, ItemStack itemStack) {
        super(EntityType.EXPERIENCE_BOTTLE, x, y, z, level, itemStack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.EXPERIENCE_BOTTLE;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.07;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel level2 = (ServerLevel)level;
            level2.levelEvent(2002, this.blockPosition(), -13083194);
            int xpCount = 3 + this.random.nextInt(5) + this.random.nextInt(5);
            if (hitResult instanceof BlockHitResult) {
                BlockHitResult blockHitResult = (BlockHitResult)hitResult;
                Vec3 blockNormalHit = blockHitResult.getDirection().getUnitVec3();
                ExperienceOrb.awardWithDirection(level2, hitResult.getLocation(), blockNormalHit, xpCount);
            } else {
                ExperienceOrb.awardWithDirection(level2, hitResult.getLocation(), this.getDeltaMovement().scale(-1.0), xpCount);
            }
            this.discard();
        }
    }
}

