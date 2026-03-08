/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.data.models.blockstates;

import net.mayaan.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.mayaan.world.level.block.Block;

public interface BlockModelDefinitionGenerator {
    public Block block();

    public BlockStateModelDispatcher create();
}

