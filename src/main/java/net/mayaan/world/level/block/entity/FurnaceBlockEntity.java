/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.entity;

import net.mayaan.core.BlockPos;
import net.mayaan.network.chat.Component;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.inventory.FurnaceMenu;
import net.mayaan.world.item.crafting.RecipeType;
import net.mayaan.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockState;

public class FurnaceBlockEntity
extends AbstractFurnaceBlockEntity {
    private static final Component DEFAULT_NAME = Component.translatable("container.furnace");

    public FurnaceBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlockEntityType.FURNACE, worldPosition, blockState, RecipeType.SMELTING);
    }

    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new FurnaceMenu(containerId, inventory, this, this.dataAccess);
    }
}

