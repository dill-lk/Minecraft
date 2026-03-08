/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.chunk;

import net.mayaan.world.level.block.state.BlockState;

public interface BlockColumn {
    public BlockState getBlock(int var1);

    public void setBlock(int var1, BlockState var2);
}

