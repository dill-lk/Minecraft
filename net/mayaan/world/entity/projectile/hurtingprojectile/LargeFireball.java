/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.projectile.hurtingprojectile;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.projectile.hurtingprojectile.Fireball;
import net.mayaan.world.item.enchantment.EnchantmentHelper;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.gamerules.GameRules;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import net.mayaan.world.phys.EntityHitResult;
import net.mayaan.world.phys.HitResult;
import net.mayaan.world.phys.Vec3;

public class LargeFireball
extends Fireball {
    private static final byte DEFAULT_EXPLOSION_POWER = 1;
    private int explosionPower = 1;

    public LargeFireball(EntityType<? extends LargeFireball> type, Level level) {
        super((EntityType<? extends Fireball>)type, level);
    }

    public LargeFireball(Level level, LivingEntity mob, Vec3 direction, int explosionPower) {
        super((EntityType<? extends Fireball>)EntityType.FIREBALL, mob, direction, level);
        this.explosionPower = explosionPower;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            boolean grief = serverLevel.getGameRules().get(GameRules.MOB_GRIEFING);
            this.level().explode((Entity)this, this.getX(), this.getY(), this.getZ(), (float)this.explosionPower, grief, Level.ExplosionInteraction.MOB);
            this.discard();
        }
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
        DamageSource damageSource = this.damageSources().fireball(this, owner);
        entity.hurtServer(serverLevel, damageSource, 6.0f);
        EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putByte("ExplosionPower", (byte)this.explosionPower);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.explosionPower = input.getByteOr("ExplosionPower", (byte)1);
    }
}

