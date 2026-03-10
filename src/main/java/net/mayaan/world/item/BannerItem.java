/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package net.mayaan.world.item;

import net.mayaan.core.Direction;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.StandingAndWallBlockItem;
import net.mayaan.world.level.block.AbstractBannerBlock;
import net.mayaan.world.level.block.Block;
import org.apache.commons.lang3.Validate;

public class BannerItem
extends StandingAndWallBlockItem {
    public BannerItem(Block block, Block wallBlock, Item.Properties properties) {
        super(block, wallBlock, Direction.DOWN, properties);
        Validate.isInstanceOf(AbstractBannerBlock.class, (Object)block);
        Validate.isInstanceOf(AbstractBannerBlock.class, (Object)wallBlock);
    }

    public DyeColor getColor() {
        return ((AbstractBannerBlock)this.getBlock()).getColor();
    }
}

