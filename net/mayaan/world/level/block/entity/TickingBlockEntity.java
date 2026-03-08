/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.entity;

import net.mayaan.core.BlockPos;

public interface TickingBlockEntity {
    public void tick();

    public boolean isRemoved();

    public BlockPos getPos();

    public String getType();
}

