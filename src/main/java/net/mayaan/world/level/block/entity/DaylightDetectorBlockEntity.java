/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.entity;

import net.mayaan.core.BlockPos;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockState;

public class DaylightDetectorBlockEntity
extends BlockEntity {
    public DaylightDetectorBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlockEntityType.DAYLIGHT_DETECTOR, worldPosition, blockState);
    }
}

