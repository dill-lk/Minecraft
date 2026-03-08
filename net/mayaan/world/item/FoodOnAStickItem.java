/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.stats.Stats;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.ItemSteerable;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;

public class FoodOnAStickItem<T extends Entity>
extends Item {
    private final EntityType<T> canInteractWith;
    private final int consumeItemDamage;

    public FoodOnAStickItem(EntityType<T> canInteractWith, int consumeItemDamage, Item.Properties properties) {
        super(properties);
        this.canInteractWith = canInteractWith;
        this.consumeItemDamage = consumeItemDamage;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }
        Entity vehicle = player.getControlledVehicle();
        if (player.isPassenger() && vehicle instanceof ItemSteerable) {
            ItemSteerable steerable = (ItemSteerable)((Object)vehicle);
            if (vehicle.is(this.canInteractWith) && steerable.boost()) {
                EquipmentSlot slot = hand.asEquipmentSlot();
                ItemStack result = itemStack.hurtAndConvertOnBreak(this.consumeItemDamage, Items.FISHING_ROD, player, slot);
                return InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(result);
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.PASS;
    }
}

