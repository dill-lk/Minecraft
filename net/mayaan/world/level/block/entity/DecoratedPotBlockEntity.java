/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block.entity;

import java.util.List;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.component.DataComponentMap;
import net.mayaan.core.component.DataComponentPatch;
import net.mayaan.core.component.DataComponents;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.RandomizableContainer;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ItemStackTemplate;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.component.ItemContainerContents;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.entity.PotDecorations;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import net.mayaan.world.level.storage.loot.LootTable;
import net.mayaan.world.ticks.ContainerSingleItem;
import org.jspecify.annotations.Nullable;

public class DecoratedPotBlockEntity
extends BlockEntity
implements ContainerSingleItem.BlockContainerSingleItem,
RandomizableContainer {
    public static final String TAG_SHERDS = "sherds";
    public static final String TAG_ITEM = "item";
    public static final int EVENT_POT_WOBBLES = 1;
    public long wobbleStartedAtTick;
    public @Nullable WobbleStyle lastWobbleStyle;
    private PotDecorations decorations;
    private ItemStack item = ItemStack.EMPTY;
    protected @Nullable ResourceKey<LootTable> lootTable;
    protected long lootTableSeed;

    public DecoratedPotBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlockEntityType.DECORATED_POT, worldPosition, blockState);
        this.decorations = PotDecorations.EMPTY;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (!this.decorations.equals(PotDecorations.EMPTY)) {
            output.store(TAG_SHERDS, PotDecorations.CODEC, this.decorations);
        }
        if (!this.trySaveLootTable(output) && !this.item.isEmpty()) {
            output.store(TAG_ITEM, ItemStack.CODEC, this.item);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.decorations = input.read(TAG_SHERDS, PotDecorations.CODEC).orElse(PotDecorations.EMPTY);
        this.item = !this.tryLoadLootTable(input) ? input.read(TAG_ITEM, ItemStack.CODEC).orElse(ItemStack.EMPTY) : ItemStack.EMPTY;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }

    public Direction getDirection() {
        return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
    }

    public PotDecorations getDecorations() {
        return this.decorations;
    }

    public static ItemStackTemplate createDecoratedPotTemplate(PotDecorations decorations) {
        return new ItemStackTemplate(Items.DECORATED_POT, DataComponentPatch.builder().set(DataComponents.POT_DECORATIONS, decorations).build());
    }

    public static ItemStack createDecoratedPotInstance(PotDecorations decorations) {
        return DecoratedPotBlockEntity.createDecoratedPotTemplate(decorations).create();
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
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.POT_DECORATIONS, this.decorations);
        components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(this.item)));
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        this.decorations = components.getOrDefault(DataComponents.POT_DECORATIONS, PotDecorations.EMPTY);
        this.item = components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyOne();
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard(TAG_SHERDS);
        output.discard(TAG_ITEM);
    }

    @Override
    public ItemStack getTheItem() {
        this.unpackLootTable(null);
        return this.item;
    }

    @Override
    public ItemStack splitTheItem(int count) {
        this.unpackLootTable(null);
        ItemStack result = this.item.split(count);
        if (this.item.isEmpty()) {
            this.item = ItemStack.EMPTY;
        }
        return result;
    }

    @Override
    public void setTheItem(ItemStack itemStack) {
        this.unpackLootTable(null);
        this.item = itemStack;
    }

    @Override
    public BlockEntity getContainerBlockEntity() {
        return this;
    }

    public void wobble(WobbleStyle wobbleStyle) {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, wobbleStyle.ordinal());
    }

    @Override
    public boolean triggerEvent(int event, int data) {
        if (this.level != null && event == 1 && data >= 0 && data < WobbleStyle.values().length) {
            this.wobbleStartedAtTick = this.level.getGameTime();
            this.lastWobbleStyle = WobbleStyle.values()[data];
            return true;
        }
        return super.triggerEvent(event, data);
    }

    public static enum WobbleStyle {
        POSITIVE(7),
        NEGATIVE(10);

        public final int duration;

        private WobbleStyle(int duration) {
            this.duration = duration;
        }
    }
}

