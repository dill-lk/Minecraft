/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.SignItem;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.WallHangingSignBlock;
import net.mayaan.world.level.block.state.BlockState;

public class HangingSignItem
extends SignItem {
    public HangingSignItem(Block hangingSign, Block wallHangingSign, Item.Properties properties) {
        super(properties, hangingSign, wallHangingSign, Direction.UP);
    }

    @Override
    protected boolean canPlace(LevelReader level, BlockState possibleState, BlockPos pos) {
        WallHangingSignBlock hangingSign;
        Block block = possibleState.getBlock();
        if (block instanceof WallHangingSignBlock && !(hangingSign = (WallHangingSignBlock)block).canPlace(possibleState, level, pos)) {
            return false;
        }
        return super.canPlace(level, possibleState, pos);
    }
}

