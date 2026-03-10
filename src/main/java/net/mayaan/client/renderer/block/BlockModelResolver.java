/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.block;

import net.mayaan.client.renderer.block.BlockModelRenderState;
import net.mayaan.client.renderer.block.model.BlockDisplayContext;
import net.mayaan.client.renderer.entity.ItemFrameRenderer;
import net.mayaan.client.resources.model.BlockStateDefinitions;
import net.mayaan.client.resources.model.ModelManager;
import net.mayaan.world.level.block.state.BlockState;

public class BlockModelResolver {
    private static final long MODEL_SEED = 42L;
    private final ModelManager modelManager;

    public BlockModelResolver(ModelManager modelManager) {
        this.modelManager = modelManager;
    }

    public void update(BlockModelRenderState renderState, BlockState blockState, BlockDisplayContext displayContext) {
        renderState.clear();
        this.modelManager.getBlockModelSet().get(blockState).update(renderState, blockState, displayContext, 42L);
    }

    public void updateForItemFrame(BlockModelRenderState renderState, boolean isGlowing, boolean map) {
        BlockState fakeState = BlockStateDefinitions.getItemFrameFakeState(isGlowing, map);
        this.update(renderState, fakeState, ItemFrameRenderer.BLOCK_DISPLAY_CONTEXT);
    }
}

