/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.item.enchantment;

import java.util.function.Consumer;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record EnchantedItemInUse(ItemStack itemStack, @Nullable EquipmentSlot inSlot, @Nullable LivingEntity owner, Consumer<Item> onBreak) {
    public EnchantedItemInUse(ItemStack itemStack, EquipmentSlot inSlot, LivingEntity owner) {
        this(itemStack, inSlot, owner, item -> owner.onEquippedItemBroken((Item)item, inSlot));
    }
}

