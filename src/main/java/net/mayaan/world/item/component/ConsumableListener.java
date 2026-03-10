/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.component;

import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.component.Consumable;
import net.mayaan.world.level.Level;

public interface ConsumableListener {
    public void onConsume(Level var1, LivingEntity var2, ItemStack var3, Consumable var4);
}

