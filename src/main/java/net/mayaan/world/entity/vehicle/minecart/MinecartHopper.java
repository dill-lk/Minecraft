/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.vehicle.minecart;

import java.util.List;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySelector;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.item.ItemEntity;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.entity.vehicle.minecart.AbstractMinecartContainer;
import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.inventory.HopperMenu;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.entity.Hopper;
import net.mayaan.world.level.block.entity.HopperBlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.RailShape;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;

public class MinecartHopper
extends AbstractMinecartContainer
implements Hopper {
    private static final boolean DEFAULT_ENABLED = true;
    private boolean enabled = true;
    private boolean consumedItemThisFrame = false;

    public MinecartHopper(EntityType<? extends MinecartHopper> type, Level level) {
        super(type, level);
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.HOPPER.defaultBlockState();
    }

    @Override
    public int getDefaultDisplayOffset() {
        return 1;
    }

    @Override
    public int getContainerSize() {
        return 5;
    }

    @Override
    public void activateMinecart(ServerLevel level, int xt, int yt, int zt, boolean state) {
        boolean newEnabled;
        boolean bl = newEnabled = !state;
        if (newEnabled != this.isEnabled()) {
            this.setEnabled(newEnabled);
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public double getLevelX() {
        return this.getX();
    }

    @Override
    public double getLevelY() {
        return this.getY() + 0.5;
    }

    @Override
    public double getLevelZ() {
        return this.getZ();
    }

    @Override
    public boolean isGridAligned() {
        return false;
    }

    @Override
    public void tick() {
        this.consumedItemThisFrame = false;
        super.tick();
        this.tryConsumeItems();
    }

    @Override
    protected double makeStepAlongTrack(BlockPos pos, RailShape shape, double movementLeft) {
        double left = super.makeStepAlongTrack(pos, shape, movementLeft);
        this.tryConsumeItems();
        return left;
    }

    private void tryConsumeItems() {
        if (!this.level().isClientSide() && this.isAlive() && this.isEnabled() && !this.consumedItemThisFrame && this.suckInItems()) {
            this.consumedItemThisFrame = true;
            this.setChanged();
        }
    }

    public boolean suckInItems() {
        if (HopperBlockEntity.suckInItems(this.level(), this)) {
            return true;
        }
        List<Entity> entities = this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(0.25, 0.0, 0.25), EntitySelector.ENTITY_STILL_ALIVE);
        for (ItemEntity itemEntity : entities) {
            if (!HopperBlockEntity.addItem(this, itemEntity)) continue;
            return true;
        }
        return false;
    }

    @Override
    protected Item getDropItem() {
        return Items.HOPPER_MINECART;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.HOPPER_MINECART);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Enabled", this.enabled);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.enabled = input.getBooleanOr("Enabled", true);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new HopperMenu(containerId, inventory, this);
    }
}

