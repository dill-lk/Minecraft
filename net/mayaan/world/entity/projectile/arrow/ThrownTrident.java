/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.projectile.arrow;

import java.util.Collection;
import java.util.List;
import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.EntityDataSerializers;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.util.Mth;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.projectile.ProjectileDeflection;
import net.mayaan.world.entity.projectile.arrow.AbstractArrow;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.enchantment.EnchantmentHelper;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.EntityHitResult;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ThrownTrident
extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BOOLEAN);
    private static final float WATER_INERTIA = 0.99f;
    private static final boolean DEFAULT_DEALT_DAMAGE = false;
    private boolean dealtDamage = false;
    public int clientSideReturnTridentTickCount;

    public ThrownTrident(EntityType<? extends ThrownTrident> type, Level level) {
        super((EntityType<? extends AbstractArrow>)type, level);
    }

    public ThrownTrident(Level level, LivingEntity owner, ItemStack tridentItem) {
        super(EntityType.TRIDENT, owner, level, tridentItem, null);
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(tridentItem));
        this.entityData.set(ID_FOIL, tridentItem.hasFoil());
    }

    public ThrownTrident(Level level, double x, double y, double z, ItemStack tridentItem) {
        super(EntityType.TRIDENT, x, y, z, level, tridentItem, tridentItem);
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(tridentItem));
        this.entityData.set(ID_FOIL, tridentItem.hasFoil());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(ID_LOYALTY, (byte)0);
        entityData.define(ID_FOIL, false);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        Entity currentOwner = this.getOwner();
        byte loyalty = this.entityData.get(ID_LOYALTY);
        if (loyalty > 0 && (this.dealtDamage || this.isNoPhysics()) && currentOwner != null) {
            if (!this.isAcceptibleReturnOwner()) {
                Level level = this.level();
                if (level instanceof ServerLevel) {
                    ServerLevel level2 = (ServerLevel)level;
                    if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
                        this.spawnAtLocation(level2, this.getPickupItem(), 0.1f);
                    }
                }
                this.discard();
            } else {
                if (!(currentOwner instanceof Player) && this.position().distanceTo(currentOwner.getEyePosition()) < (double)currentOwner.getBbWidth() + 1.0) {
                    this.discard();
                    return;
                }
                this.setNoPhysics(true);
                Vec3 vec = currentOwner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec.y * 0.015 * (double)loyalty, this.getZ());
                double accel = 0.05 * (double)loyalty;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec.normalize().scale(accel)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0f, 1.0f);
                }
                ++this.clientSideReturnTridentTickCount;
            }
        }
        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity currentOwner = this.getOwner();
        if (currentOwner == null || !currentOwner.isAlive()) {
            return false;
        }
        return !(currentOwner instanceof ServerPlayer) || !currentOwner.isSpectator();
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Override
    protected @Nullable EntityHitResult findHitEntity(Vec3 from, Vec3 to) {
        if (this.dealtDamage) {
            return null;
        }
        return super.findHitEntity(from, to);
    }

    @Override
    protected Collection<EntityHitResult> findHitEntities(Vec3 from, Vec3 to) {
        EntityHitResult e = this.findHitEntity(from, to);
        if (e != null) {
            return List.of(e);
        }
        return List.of();
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        ServerLevel serverLevel;
        Entity entity = hitResult.getEntity();
        float dmg = 8.0f;
        Entity currentOwner = this.getOwner();
        DamageSource damageSource = this.damageSources().trident(this, currentOwner == null ? this : currentOwner);
        Level level = this.level();
        if (level instanceof ServerLevel) {
            serverLevel = (ServerLevel)level;
            dmg = EnchantmentHelper.modifyDamage(serverLevel, this.getWeaponItem(), entity, damageSource, dmg);
        }
        this.dealtDamage = true;
        if (entity.hurtOrSimulate(damageSource, dmg)) {
            if (entity.is(EntityType.ENDERMAN)) {
                return;
            }
            level = this.level();
            if (level instanceof ServerLevel) {
                serverLevel = (ServerLevel)level;
                EnchantmentHelper.doPostAttackEffectsWithItemSourceOnBreak(serverLevel, entity, damageSource, this.getWeaponItem(), weapon -> this.kill(serverLevel));
            }
            if (entity instanceof LivingEntity) {
                LivingEntity mob = (LivingEntity)entity;
                this.doKnockback(mob, damageSource);
                this.doPostHurtEffects(mob);
            }
        }
        this.deflect(ProjectileDeflection.REVERSE, entity, this.owner, false);
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.02, 0.2, 0.02));
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0f, 1.0f);
    }

    @Override
    protected void hitBlockEnchantmentEffects(ServerLevel level, BlockHitResult hitResult, ItemStack weapon) {
        LivingEntity livingOwner;
        Vec3 compensatedHitPosition = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
        Entity entity = this.getOwner();
        EnchantmentHelper.onHitBlock(level, weapon, entity instanceof LivingEntity ? (livingOwner = (LivingEntity)entity) : null, this, null, compensatedHitPosition, level.getBlockState(hitResult.getBlockPos()), item -> this.kill(level));
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.TRIDENT);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.dealtDamage = input.getBooleanOr("DealtDamage", false);
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(this.getPickupItemStackOrigin()));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("DealtDamage", this.dealtDamage);
    }

    private byte getLoyaltyFromItem(ItemStack tridentItem) {
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            return (byte)Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverLevel, tridentItem, this), 0, 127);
        }
        return 0;
    }

    @Override
    public void tickDespawn() {
        byte loyalty = this.entityData.get(ID_LOYALTY);
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || loyalty <= 0) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.99f;
    }

    @Override
    public boolean shouldRender(double camX, double camY, double camZ) {
        return true;
    }
}

