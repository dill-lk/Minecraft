/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.color.block;

import java.util.Set;
import net.mayaan.client.renderer.block.BlockAndTintGetter;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.Property;

public interface BlockTintSource {
    public int color(BlockState var1);

    default public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return this.color(state);
    }

    default public int colorAsTerrainParticle(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return this.colorInWorld(state, level, pos);
    }

    default public Set<Property<?>> relevantProperties() {
        return Set.of();
    }
}

