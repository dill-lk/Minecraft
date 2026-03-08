/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.item;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.StandingAndWallBlockItem;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.SignBlock;
import net.mayaan.world.level.block.entity.SignBlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class SignItem
extends StandingAndWallBlockItem {
    public SignItem(Block sign, Block wallSign, Item.Properties properties) {
        super(sign, wallSign, Direction.DOWN, properties);
    }

    public SignItem(Item.Properties properties, Block sign, Block wallSign, Direction direction) {
        super(sign, wallSign, direction, properties);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack itemStack, BlockState placedState) {
        Object object;
        boolean success = super.updateCustomBlockEntityTag(pos, level, player, itemStack, placedState);
        if (!level.isClientSide() && !success && player != null && (object = level.getBlockEntity(pos)) instanceof SignBlockEntity) {
            SignBlockEntity signEntity = (SignBlockEntity)object;
            object = level.getBlockState(pos).getBlock();
            if (object instanceof SignBlock) {
                SignBlock sign = (SignBlock)object;
                sign.openTextEdit(player, signEntity, true);
            }
        }
        return success;
    }
}

