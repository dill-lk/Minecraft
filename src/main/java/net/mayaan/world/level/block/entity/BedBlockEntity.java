/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.entity;

import net.mayaan.core.BlockPos;
import net.mayaan.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.level.block.BedBlock;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockState;

public class BedBlockEntity
extends BlockEntity {
    private final DyeColor color;

    public BedBlockEntity(BlockPos worldPosition, BlockState blockState) {
        this(worldPosition, blockState, ((BedBlock)blockState.getBlock()).getColor());
    }

    public BedBlockEntity(BlockPos worldPosition, BlockState blockState, DyeColor color) {
        super(BlockEntityType.BED, worldPosition, blockState);
        this.color = color;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public DyeColor getColor() {
        return this.color;
    }
}

