/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.vehicle.minecart;

import net.mayaan.core.Direction;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.ContainerUser;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.monster.piglin.PiglinAi;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.vehicle.minecart.AbstractMinecartContainer;
import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.inventory.ChestMenu;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.ChestBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.phys.Vec3;

public class MinecartChest
extends AbstractMinecartContainer {
    public MinecartChest(EntityType<? extends MinecartChest> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDropItem() {
        return Items.CHEST_MINECART;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.CHEST_MINECART);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return (BlockState)Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH);
    }

    @Override
    public int getDefaultDisplayOffset() {
        return 8;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return ChestMenu.threeRows(containerId, inventory, this);
    }

    @Override
    public void stopOpen(ContainerUser containerUser) {
        this.level().gameEvent(GameEvent.CONTAINER_CLOSE, this.position(), GameEvent.Context.of(containerUser.getLivingEntity()));
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        Level level;
        InteractionResult result = this.interactWithContainerVehicle(player);
        if (result.consumesAction() && (level = player.level()) instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            this.gameEvent(GameEvent.CONTAINER_OPEN, player);
            PiglinAi.angerNearbyPiglins(serverLevel, player, true);
        }
        return result;
    }
}

