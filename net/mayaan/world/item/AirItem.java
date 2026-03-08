/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.component.DataComponents;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.block.Block;

public class AirItem
extends Item {
    public AirItem(Block block, Item.Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return itemStack.typeHolder().components().getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY);
    }
}

