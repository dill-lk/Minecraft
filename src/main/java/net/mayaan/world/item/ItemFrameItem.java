/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.decoration.HangingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.HangingEntityItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;

public class ItemFrameItem
extends HangingEntityItem {
    public ItemFrameItem(EntityType<? extends HangingEntity> entityType, Item.Properties properties) {
        super(entityType, properties);
    }

    @Override
    protected boolean mayPlace(Player player, Direction direction, ItemStack itemStack, BlockPos blockPos) {
        return player.level().isInsideBuildHeight(blockPos) && player.mayUseItemAt(blockPos, direction, itemStack);
    }
}

