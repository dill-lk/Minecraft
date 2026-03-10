/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.item;

import net.mayaan.core.Direction;
import net.mayaan.core.Position;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.projectile.Projectile;
import net.mayaan.world.entity.projectile.arrow.AbstractArrow;
import net.mayaan.world.entity.projectile.arrow.SpectralArrow;
import net.mayaan.world.item.ArrowItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import org.jspecify.annotations.Nullable;

public class SpectralArrowItem
extends ArrowItem {
    public SpectralArrowItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack itemStack, LivingEntity owner, @Nullable ItemStack firedFromWeapon) {
        return new SpectralArrow(level, owner, itemStack.copyWithCount(1), firedFromWeapon);
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        SpectralArrow arrow = new SpectralArrow(level, position.x(), position.y(), position.z(), itemStack.copyWithCount(1), null);
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }
}

