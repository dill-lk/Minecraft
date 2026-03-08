/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.component.DataComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;

public class NameTagItem
extends Item {
    public NameTagItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type) {
        Component customName = itemStack.get(DataComponents.CUSTOM_NAME);
        if (customName != null && target.getType().canSerialize()) {
            if (!player.level().isClientSide() && target.isAlive()) {
                target.setCustomName(customName);
                if (target instanceof Mob) {
                    Mob mob = (Mob)target;
                    mob.setPersistenceRequired();
                }
                itemStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}

