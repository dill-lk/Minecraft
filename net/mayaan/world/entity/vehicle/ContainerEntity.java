/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.vehicle;

import java.util.Objects;
import net.mayaan.advancements.CriteriaTriggers;
import net.mayaan.core.NonNullList;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.Container;
import net.mayaan.world.ContainerHelper;
import net.mayaan.world.Containers;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.MenuProvider;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.SlotAccess;
import net.mayaan.world.entity.monster.piglin.PiglinAi;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.gamerules.GameRules;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import net.mayaan.world.level.storage.loot.LootParams;
import net.mayaan.world.level.storage.loot.LootTable;
import net.mayaan.world.level.storage.loot.parameters.LootContextParamSets;
import net.mayaan.world.level.storage.loot.parameters.LootContextParams;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public interface ContainerEntity
extends Container,
MenuProvider {
    public Vec3 position();

    public AABB getBoundingBox();

    public @Nullable ResourceKey<LootTable> getContainerLootTable();

    public void setContainerLootTable(@Nullable ResourceKey<LootTable> var1);

    public long getContainerLootTableSeed();

    public void setContainerLootTableSeed(long var1);

    public NonNullList<ItemStack> getItemStacks();

    public void clearItemStacks();

    public Level level();

    public boolean isRemoved();

    @Override
    default public boolean isEmpty() {
        return this.isChestVehicleEmpty();
    }

    default public void addChestVehicleSaveData(ValueOutput output) {
        if (this.getContainerLootTable() != null) {
            output.putString("LootTable", this.getContainerLootTable().identifier().toString());
            if (this.getContainerLootTableSeed() != 0L) {
                output.putLong("LootTableSeed", this.getContainerLootTableSeed());
            }
        } else {
            ContainerHelper.saveAllItems(output, this.getItemStacks());
        }
    }

    default public void readChestVehicleSaveData(ValueInput input) {
        this.clearItemStacks();
        ResourceKey lootTable = input.read("LootTable", LootTable.KEY_CODEC).orElse(null);
        this.setContainerLootTable(lootTable);
        this.setContainerLootTableSeed(input.getLongOr("LootTableSeed", 0L));
        if (lootTable == null) {
            ContainerHelper.loadAllItems(input, this.getItemStacks());
        }
    }

    default public void chestVehicleDestroyed(DamageSource source, ServerLevel level, Entity entity) {
        if (!level.getGameRules().get(GameRules.ENTITY_DROPS).booleanValue()) {
            return;
        }
        Containers.dropContents((Level)level, entity, (Container)this);
        Entity directEntity = source.getDirectEntity();
        if (directEntity instanceof Player) {
            Player player = (Player)directEntity;
            PiglinAi.angerNearbyPiglins(level, player, true);
        }
    }

    default public InteractionResult interactWithContainerVehicle(Player player) {
        player.openMenu(this);
        return InteractionResult.SUCCESS;
    }

    default public void unpackChestVehicleLootTable(@Nullable Player player) {
        MayaanServer server = this.level().getServer();
        if (this.getContainerLootTable() != null && server != null) {
            LootTable lootTable = server.reloadableRegistries().getLootTable(this.getContainerLootTable());
            if (player != null) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)player, this.getContainerLootTable());
            }
            this.setContainerLootTable(null);
            LootParams.Builder builder = new LootParams.Builder((ServerLevel)this.level()).withParameter(LootContextParams.ORIGIN, this.position());
            if (player != null) {
                builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
            }
            lootTable.fill(this, builder.create(LootContextParamSets.CHEST), this.getContainerLootTableSeed());
        }
    }

    default public void clearChestVehicleContent() {
        this.unpackChestVehicleLootTable(null);
        this.getItemStacks().clear();
    }

    default public boolean isChestVehicleEmpty() {
        for (ItemStack itemStack : this.getItemStacks()) {
            if (itemStack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    default public ItemStack removeChestVehicleItemNoUpdate(int slot) {
        this.unpackChestVehicleLootTable(null);
        ItemStack itemStack = this.getItemStacks().get(slot);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.getItemStacks().set(slot, ItemStack.EMPTY);
        return itemStack;
    }

    default public ItemStack getChestVehicleItem(int slot) {
        this.unpackChestVehicleLootTable(null);
        return this.getItemStacks().get(slot);
    }

    default public ItemStack removeChestVehicleItem(int slot, int count) {
        this.unpackChestVehicleLootTable(null);
        return ContainerHelper.removeItem(this.getItemStacks(), slot, count);
    }

    default public void setChestVehicleItem(int slot, ItemStack itemStack) {
        this.unpackChestVehicleLootTable(null);
        this.getItemStacks().set(slot, itemStack);
        itemStack.limitSize(this.getMaxStackSize(itemStack));
    }

    default public @Nullable SlotAccess getChestVehicleSlot(final int slot) {
        if (slot >= 0 && slot < this.getContainerSize()) {
            return new SlotAccess(){
                final /* synthetic */ ContainerEntity this$0;
                {
                    ContainerEntity containerEntity = this$0;
                    Objects.requireNonNull(containerEntity);
                    this.this$0 = containerEntity;
                }

                @Override
                public ItemStack get() {
                    return this.this$0.getChestVehicleItem(slot);
                }

                @Override
                public boolean set(ItemStack itemStack) {
                    this.this$0.setChestVehicleItem(slot, itemStack);
                    return true;
                }
            };
        }
        return null;
    }

    default public boolean isChestVehicleStillValid(Player player) {
        return !this.isRemoved() && player.isWithinEntityInteractionRange(this.getBoundingBox(), 4.0);
    }
}

