/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.BlockItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.context.UseOnContext;
import net.mayaan.world.level.ClipContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.phys.BlockHitResult;

public class PlaceOnWaterBlockItem
extends BlockItem {
    public PlaceOnWaterBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        BlockHitResult hitResult = PlaceOnWaterBlockItem.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockHitResult blockAboveResult = hitResult.withPosition(hitResult.getBlockPos().above());
        return super.useOn(new UseOnContext(player, hand, blockAboveResult));
    }
}

