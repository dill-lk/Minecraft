/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.block.model.properties.select;

import net.mayaan.client.renderer.block.model.BlockDisplayContext;
import net.mayaan.client.renderer.block.model.properties.select.SelectBlockModelProperty;
import net.mayaan.world.level.block.state.BlockState;

public record DisplayContext() implements SelectBlockModelProperty<BlockDisplayContext>
{
    @Override
    public BlockDisplayContext get(BlockState blockState, BlockDisplayContext displayContext) {
        return displayContext;
    }
}

