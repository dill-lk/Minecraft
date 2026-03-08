/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block.entity;

import net.mayaan.core.BlockPos;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.component.DataComponentMap;
import net.mayaan.core.component.DataComponents;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.RandomizableContainer;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.component.SeededContainerLoot;
import net.mayaan.world.level.block.entity.BaseContainerBlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.storage.ValueOutput;
import net.mayaan.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;

public abstract class RandomizableContainerBlockEntity
extends BaseContainerBlockEntity
implements RandomizableContainer {
    protected @Nullable ResourceKey<LootTable> lootTable;
    protected long lootTableSeed = 0L;

    protected RandomizableContainerBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    @Override
    public @Nullable ResourceKey<LootTable> getLootTable() {
        return this.lootTable;
    }

    @Override
    public void setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
        this.lootTable = lootTable;
    }

    @Override
    public long getLootTableSeed() {
        return this.lootTableSeed;
    }

    @Override
    public void setLootTableSeed(long lootTableSeed) {
        this.lootTableSeed = lootTableSeed;
    }

    @Override
    public boolean isEmpty() {
        this.unpackLootTable(null);
        return super.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        this.unpackLootTable(null);
        return super.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        this.unpackLootTable(null);
        return super.removeItem(slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        this.unpackLootTable(null);
        return super.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.unpackLootTable(null);
        super.setItem(slot, itemStack);
    }

    @Override
    public boolean canOpen(Player player) {
        return (this.lootTable == null || !player.isSpectator()) && super.canOpen(player);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        if (this.canOpen(player)) {
            this.unpackLootTable(inventory.player);
            return this.createMenu(containerId, inventory);
        }
        if (!player.isSpectator()) {
            BaseContainerBlockEntity.sendChestLockedNotifications(this.getBlockPos().getCenter(), player, this.getDisplayName());
        }
        return null;
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        SeededContainerLoot loot = components.get(DataComponents.CONTAINER_LOOT);
        if (loot != null) {
            this.lootTable = loot.lootTable();
            this.lootTableSeed = loot.seed();
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (this.lootTable != null) {
            components.set(DataComponents.CONTAINER_LOOT, new SeededContainerLoot(this.lootTable, this.lootTableSeed));
        }
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard("LootTable");
        output.discard("LootTableSeed");
    }
}

