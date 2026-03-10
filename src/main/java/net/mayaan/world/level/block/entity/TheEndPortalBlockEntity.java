/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.entity;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockState;

public class TheEndPortalBlockEntity
extends BlockEntity {
    protected TheEndPortalBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    public TheEndPortalBlockEntity(BlockPos worldPosition, BlockState blockState) {
        this(BlockEntityType.END_PORTAL, worldPosition, blockState);
    }

    public boolean shouldRenderFace(Direction direction) {
        return direction.getAxis() == Direction.Axis.Y;
    }
}

