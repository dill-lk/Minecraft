/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.item;

import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.BlockItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class GameMasterBlockItem
extends BlockItem {
    public GameMasterBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    protected @Nullable BlockState getPlacementState(BlockPlaceContext context) {
        Player player = context.getPlayer();
        return player == null || player.canUseGameMasterBlocks() ? super.getPlacementState(context) : null;
    }
}

