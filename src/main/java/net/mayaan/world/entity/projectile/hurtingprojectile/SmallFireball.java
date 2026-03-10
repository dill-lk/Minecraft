/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.projectile.hurtingprojectile;

import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.projectile.hurtingprojectile.Fireball;
import net.mayaan.world.item.enchantment.EnchantmentHelper;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.BaseFireBlock;
import net.mayaan.world.level.gamerules.GameRules;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.EntityHitResult;
import net.mayaan.world.phys.HitResult;
import net.mayaan.world.phys.Vec3;

public class SmallFireball
extends Fireball {
    public SmallFireball(EntityType<? extends SmallFireball> type, Level level) {
        super((EntityType<? extends Fireball>)type, level);
    }

    public SmallFireball(Level level, LivingEntity mob, Vec3 direction) {
        super((EntityType<? extends Fireball>)EntityType.SMALL_FIREBALL, mob, direction, level);
    }

    public SmallFireball(Level level, double x, double y, double z, Vec3 direction) {
        super((EntityType<? extends Fireball>)EntityType.SMALL_FIREBALL, x, y, z, direction, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Level level = this.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        Entity entity = hitResult.getEntity();
        Entity owner = this.getOwner();
        int remainingFireTicks = entity.getRemainingFireTicks();
        entity.igniteForSeconds(5.0f);
        DamageSource damageSource = this.damageSources().fireball(this, owner);
        if (!entity.hurtServer(serverLevel, damageSource, 5.0f)) {
            entity.setRemainingFireTicks(remainingFireTicks);
        } else {
            EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        Level level = this.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        Entity owner = this.getOwner();
        if (!(owner instanceof Mob) || serverLevel.getGameRules().get(GameRules.MOB_GRIEFING).booleanValue()) {
            BlockPos pos = hitResult.getBlockPos().relative(hitResult.getDirection());
            if (this.level().isEmptyBlock(pos)) {
                this.level().setBlockAndUpdate(pos, BaseFireBlock.getState(this.level(), pos));
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }
}

