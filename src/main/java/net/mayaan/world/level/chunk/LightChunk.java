/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.chunk;

import java.util.function.BiConsumer;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.lighting.ChunkSkyLightSources;

public interface LightChunk
extends BlockGetter {
    public void findBlockLightSources(BiConsumer<BlockPos, BlockState> var1);

    public ChunkSkyLightSources getSkyLightSources();
}

