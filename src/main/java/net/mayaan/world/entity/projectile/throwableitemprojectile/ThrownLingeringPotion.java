/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.projectile.throwableitemprojectile;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.AreaEffectCloud;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.phys.HitResult;

public class ThrownLingeringPotion
extends AbstractThrownPotion {
    public ThrownLingeringPotion(EntityType<? extends ThrownLingeringPotion> type, Level level) {
        super((EntityType<? extends AbstractThrownPotion>)type, level);
    }

    public ThrownLingeringPotion(Level level, LivingEntity owner, ItemStack itemStack) {
        super(EntityType.LINGERING_POTION, level, owner, itemStack);
    }

    public ThrownLingeringPotion(Level level, double x, double y, double z, ItemStack itemStack) {
        super(EntityType.LINGERING_POTION, level, x, y, z, itemStack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.LINGERING_POTION;
    }

    @Override
    public void onHitAsPotion(ServerLevel level, ItemStack potionItem, HitResult hitResult) {
        AreaEffectCloud cloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            LivingEntity owner = (LivingEntity)entity;
            cloud.setOwner(owner);
        }
        cloud.setRadius(3.0f);
        cloud.setRadiusOnUse(-0.5f);
        cloud.setDuration(600);
        cloud.setWaitTime(10);
        cloud.setRadiusPerTick(-cloud.getRadius() / (float)cloud.getDuration());
        cloud.applyComponentsFromItemStack(potionItem);
        level.addFreshEntity(cloud);
    }
}

