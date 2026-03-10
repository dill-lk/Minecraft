/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.entity;

import net.mayaan.core.BlockPos;
import net.mayaan.network.chat.Component;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.entity.DispenserBlockEntity;
import net.mayaan.world.level.block.state.BlockState;

public class DropperBlockEntity
extends DispenserBlockEntity {
    private static final Component DEFAULT_NAME = Component.translatable("container.dropper");

    public DropperBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlockEntityType.DROPPER, worldPosition, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }
}

