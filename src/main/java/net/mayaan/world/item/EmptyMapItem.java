/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.stats.Stats;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.MapItem;
import net.mayaan.world.level.Level;

public class EmptyMapItem
extends Item {
    public EmptyMapItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        itemStack.consume(1, player);
        player.awardStat(Stats.ITEM_USED.get(this));
        serverLevel.playSound(null, player, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, player.getSoundSource(), 1.0f, 1.0f);
        ItemStack map = MapItem.create(serverLevel, player.getBlockX(), player.getBlockZ(), (byte)0, true, false);
        if (itemStack.isEmpty()) {
            return InteractionResult.SUCCESS.heldItemTransformedTo(map);
        }
        if (!player.getInventory().add(map.copy())) {
            player.drop(map, false);
        }
        return InteractionResult.SUCCESS;
    }
}

