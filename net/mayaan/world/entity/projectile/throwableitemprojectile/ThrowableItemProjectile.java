/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.projectile.throwableitemprojectile;

import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.EntityDataSerializers;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.projectile.ItemSupplier;
import net.mayaan.world.entity.projectile.ThrowableProjectile;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;

public abstract class ThrowableItemProjectile
extends ThrowableProjectile
implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(ThrowableItemProjectile.class, EntityDataSerializers.ITEM_STACK);

    public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super((EntityType<? extends ThrowableProjectile>)type, level);
    }

    public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> type, double x, double y, double z, Level level, ItemStack itemStack) {
        super(type, x, y, z, level);
        this.setItem(itemStack);
    }

    public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> type, LivingEntity owner, Level level, ItemStack itemStack) {
        this(type, owner.getX(), owner.getEyeY() - (double)0.1f, owner.getZ(), level, itemStack);
        this.setOwner(owner);
    }

    public void setItem(ItemStack source) {
        this.getEntityData().set(DATA_ITEM_STACK, source.copyWithCount(1));
    }

    protected abstract Item getDefaultItem();

    @Override
    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(DATA_ITEM_STACK, new ItemStack(this.getDefaultItem()));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("Item", ItemStack.CODEC, this.getItem());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setItem(input.read("Item", ItemStack.CODEC).orElseGet(() -> new ItemStack(this.getDefaultItem())));
    }
}

