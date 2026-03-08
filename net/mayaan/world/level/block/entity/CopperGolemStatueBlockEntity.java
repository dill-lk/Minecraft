/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block.entity;

import net.mayaan.core.BlockPos;
import net.mayaan.core.component.DataComponentMap;
import net.mayaan.core.component.DataComponents;
import net.mayaan.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.animal.golem.CopperGolem;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.component.BlockItemStateProperties;
import net.mayaan.world.level.block.CopperGolemStatueBlock;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class CopperGolemStatueBlockEntity
extends BlockEntity {
    public CopperGolemStatueBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlockEntityType.COPPER_GOLEM_STATUE, worldPosition, blockState);
    }

    public void createStatue(CopperGolem copperGolem) {
        this.setComponents(DataComponentMap.builder().addAll(this.components()).set(DataComponents.CUSTOM_NAME, copperGolem.getCustomName()).build());
        super.setChanged();
    }

    public @Nullable CopperGolem removeStatue(BlockState state) {
        CopperGolem copperGolem = EntityType.COPPER_GOLEM.create(this.level, EntitySpawnReason.TRIGGERED);
        if (copperGolem != null) {
            copperGolem.setCustomName(this.components().get(DataComponents.CUSTOM_NAME));
            return this.initCopperGolem(state, copperGolem);
        }
        return null;
    }

    private CopperGolem initCopperGolem(BlockState state, CopperGolem copperGolem) {
        BlockPos blockPos = this.getBlockPos();
        copperGolem.snapTo(blockPos.getCenter().x, blockPos.getY(), blockPos.getCenter().z, state.getValue(CopperGolemStatueBlock.FACING).toYRot(), 0.0f);
        copperGolem.yHeadRot = copperGolem.getYRot();
        copperGolem.yBodyRot = copperGolem.getYRot();
        copperGolem.playSpawnSound();
        return copperGolem;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public ItemStack getItem(ItemStack itemStack, CopperGolemStatueBlock.Pose pose) {
        itemStack.applyComponents(this.collectComponents());
        itemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(CopperGolemStatueBlock.POSE, pose));
        return itemStack;
    }
}

