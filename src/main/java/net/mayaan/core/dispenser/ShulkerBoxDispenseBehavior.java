/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  org.slf4j.Logger
 */
package net.mayaan.core.dispenser;

import com.mojang.logging.LogUtils;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.dispenser.BlockSource;
import net.mayaan.core.dispenser.OptionalDispenseItemBehavior;
import net.mayaan.world.item.BlockItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.context.DirectionalPlaceContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.DispenserBlock;
import org.slf4j.Logger;

public class ShulkerBoxDispenseBehavior
extends OptionalDispenseItemBehavior {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    protected ItemStack execute(BlockSource source, ItemStack dispensed) {
        this.setSuccess(false);
        Item item = dispensed.getItem();
        if (item instanceof BlockItem) {
            Direction facing = source.state().getValue(DispenserBlock.FACING);
            BlockPos relativePos = source.pos().relative(facing);
            Direction clickedFace = source.level().isEmptyBlock(relativePos.below()) ? facing : Direction.UP;
            try {
                this.setSuccess(((BlockItem)item).place(new DirectionalPlaceContext((Level)source.level(), relativePos, facing, dispensed, clickedFace)).consumesAction());
            }
            catch (Exception e) {
                LOGGER.error("Error trying to place shulker box at {}", (Object)relativePos, (Object)e);
            }
        }
        return dispensed;
    }
}

