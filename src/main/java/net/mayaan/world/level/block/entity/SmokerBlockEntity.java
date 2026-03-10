/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.entity;

import net.mayaan.core.BlockPos;
import net.mayaan.network.chat.Component;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.inventory.SmokerMenu;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.crafting.RecipeType;
import net.mayaan.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.entity.FuelValues;
import net.mayaan.world.level.block.state.BlockState;

public class SmokerBlockEntity
extends AbstractFurnaceBlockEntity {
    private static final Component DEFAULT_NAME = Component.translatable("container.smoker");

    public SmokerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlockEntityType.SMOKER, worldPosition, blockState, RecipeType.SMOKING);
    }

    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    protected int getBurnDuration(FuelValues fuelValues, ItemStack itemStack) {
        return super.getBurnDuration(fuelValues, itemStack) / 2;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new SmokerMenu(containerId, inventory, this, this.dataAccess);
    }
}

