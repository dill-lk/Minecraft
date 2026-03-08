/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.monster;

import net.mayaan.world.InteractionHand;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.monster.RangedAttackMob;
import net.mayaan.world.entity.projectile.ProjectileUtil;
import net.mayaan.world.item.CrossbowItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import org.jspecify.annotations.Nullable;

public interface CrossbowAttackMob
extends RangedAttackMob {
    public void setChargingCrossbow(boolean var1);

    public @Nullable LivingEntity getTarget();

    public void onCrossbowAttackPerformed();

    default public void performCrossbowAttack(LivingEntity body, float crossbowPower) {
        InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(body, Items.CROSSBOW);
        ItemStack usedItem = body.getItemInHand(hand);
        Item item = usedItem.getItem();
        if (item instanceof CrossbowItem) {
            CrossbowItem crossbow = (CrossbowItem)item;
            crossbow.performShooting(body.level(), body, hand, usedItem, crossbowPower, 14 - body.level().getDifficulty().getId() * 4, this.getTarget());
        }
        this.onCrossbowAttackPerformed();
    }
}

