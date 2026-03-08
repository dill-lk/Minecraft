/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.BlockPos;
import net.mayaan.world.item.BlockItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;

public class DoubleHighBlockItem
extends BlockItem {
    public DoubleHighBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState placementState) {
        BlockPos above;
        Level level = context.getLevel();
        BlockState aboveState = level.isWaterAt(above = context.getClickedPos().above()) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
        level.setBlock(above, aboveState, 27);
        return super.placeBlock(context, placementState);
    }
}

