/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.block.model;

import net.mayaan.client.renderer.block.BlockModelRenderState;
import net.mayaan.client.renderer.block.model.BlockDisplayContext;
import net.mayaan.client.renderer.block.model.BlockModel;
import net.mayaan.world.level.block.state.BlockState;

public class EmptyBlockModel
implements BlockModel {
    public static final BlockModel INSTANCE = new EmptyBlockModel();

    @Override
    public void update(BlockModelRenderState output, BlockState blockState, BlockDisplayContext displayContext, long seed) {
    }
}

