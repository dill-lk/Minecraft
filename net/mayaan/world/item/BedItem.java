/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.world.item.BlockItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;

public class BedItem
extends BlockItem {
    public BedItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState placementState) {
        return context.getLevel().setBlock(context.getClickedPos(), placementState, 26);
    }
}

