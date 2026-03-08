/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.Direction;
import net.mayaan.core.component.DataComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.StandingAndWallBlockItem;
import net.mayaan.world.item.component.ResolvableProfile;
import net.mayaan.world.level.block.Block;

public class PlayerHeadItem
extends StandingAndWallBlockItem {
    public PlayerHeadItem(Block block, Block wallBlock, Item.Properties properties) {
        super(block, wallBlock, Direction.DOWN, properties);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        ResolvableProfile profile = itemStack.get(DataComponents.PROFILE);
        if (profile != null && profile.name().isPresent()) {
            return Component.translatable(this.descriptionId + ".named", profile.name().get());
        }
        return super.getName(itemStack);
    }
}

