/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair
 */
package net.mayaan.world.entity.projectile.throwableitemprojectile;

import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import java.util.List;
import java.util.function.Predicate;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.component.DataComponents;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.tags.BlockTags;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.animal.axolotl.Axolotl;
import net.mayaan.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.alchemy.PotionContents;
import net.mayaan.world.item.alchemy.Potions;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.AbstractCandleBlock;
import net.mayaan.world.level.block.CampfireBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.HitResult;

public abstract class AbstractThrownPotion
extends ThrowableItemProjectile {
    public static final double SPLASH_RANGE = 4.0;
    protected static final double SPLASH_RANGE_SQ = 16.0;
    public static final Predicate<LivingEntity> WATER_SENSITIVE_OR_ON_FIRE = livingEntity -> livingEntity.isSensitiveToWater() || livingEntity.isOnFire();

    public AbstractThrownPotion(EntityType<? extends AbstractThrownPotion> type, Level level) {
        super((EntityType<? extends ThrowableItemProjectile>)type, level);
    }

    public AbstractThrownPotion(EntityType<? extends AbstractThrownPotion> type, Level level, LivingEntity owner, ItemStack itemStack) {
        super(type, owner, level, itemStack);
    }

    public AbstractThrownPotion(EntityType<? extends AbstractThrownPotion> type, Level level, double x, double y, double z, ItemStack itemStack) {
        super(type, x, y, z, level, itemStack);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.05;
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (this.level().isClientSide()) {
            return;
        }
        ItemStack potionItemStack = this.getItem();
        Direction hitDirection = hitResult.getDirection();
        BlockPos blockHitPos = hitResult.getBlockPos();
        BlockPos blockEffectPos = blockHitPos.relative(hitDirection);
        PotionContents potion = potionItemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        if (potion.is(Potions.WATER)) {
            this.dowseFire(blockEffectPos);
            this.dowseFire(blockEffectPos.relative(hitDirection.getOpposite()));
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                this.dowseFire(blockEffectPos.relative(direction));
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        Level level = this.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel level2 = (ServerLevel)level;
        ItemStack potionItemStack = this.getItem();
        PotionContents potion = potionItemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        if (potion.is(Potions.WATER)) {
            this.onHitAsWater(level2);
        } else if (potion.hasEffects()) {
            this.onHitAsPotion(level2, potionItemStack, hitResult);
        }
        int type = potion.potion().isPresent() && potion.potion().get().value().hasInstantEffects() ? 2007 : 2002;
        level2.levelEvent(type, this.blockPosition(), potion.getColor());
        this.discard();
    }

    private void onHitAsWater(ServerLevel level) {
        AABB aabb = this.getBoundingBox().inflate(4.0, 2.0, 4.0);
        List<LivingEntity> burningOrWaterSensitiveEntities = this.level().getEntitiesOfClass(LivingEntity.class, aabb, WATER_SENSITIVE_OR_ON_FIRE);
        for (LivingEntity entity : burningOrWaterSensitiveEntities) {
            double dist = this.distanceToSqr(entity);
            if (!(dist < 16.0)) continue;
            if (entity.isSensitiveToWater()) {
                entity.hurtServer(level, this.damageSources().indirectMagic(this, this.getOwner()), 1.0f);
            }
            if (!entity.isOnFire() || !entity.isAlive()) continue;
            entity.extinguishFire();
        }
        List<Axolotl> axolotlEntities = this.level().getEntitiesOfClass(Axolotl.class, aabb);
        for (Axolotl axolotl : axolotlEntities) {
            axolotl.rehydrate();
        }
    }

    protected abstract void onHitAsPotion(ServerLevel var1, ItemStack var2, HitResult var3);

    private void dowseFire(BlockPos pos) {
        BlockState blockState = this.level().getBlockState(pos);
        if (blockState.is(BlockTags.FIRE)) {
            this.level().destroyBlock(pos, false, this);
        } else if (AbstractCandleBlock.isLit(blockState)) {
            AbstractCandleBlock.extinguish(null, blockState, this.level(), pos);
        } else if (CampfireBlock.isLitCampfire(blockState)) {
            this.level().levelEvent(null, 1009, pos, 0);
            CampfireBlock.dowse(this.getOwner(), this.level(), pos, blockState);
            this.level().setBlockAndUpdate(pos, (BlockState)blockState.setValue(CampfireBlock.LIT, false));
        }
    }

    @Override
    public DoubleDoubleImmutablePair calculateHorizontalHurtKnockbackDirection(LivingEntity hurtEntity, DamageSource damageSource) {
        double dx = hurtEntity.position().x - this.position().x;
        double dz = hurtEntity.position().z - this.position().z;
        return DoubleDoubleImmutablePair.of((double)dx, (double)dz);
    }
}

