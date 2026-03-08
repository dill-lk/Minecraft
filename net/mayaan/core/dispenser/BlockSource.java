/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.core.dispenser;

import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.level.block.entity.DispenserBlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.Vec3;

public record BlockSource(ServerLevel level, BlockPos pos, BlockState state, DispenserBlockEntity blockEntity) {
    public Vec3 center() {
        return this.pos.getCenter();
    }
}

