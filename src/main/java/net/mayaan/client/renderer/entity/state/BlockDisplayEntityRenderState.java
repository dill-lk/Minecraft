/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.block.BlockModelRenderState;
import net.mayaan.client.renderer.entity.state.DisplayEntityRenderState;

public class BlockDisplayEntityRenderState
extends DisplayEntityRenderState {
    public final BlockModelRenderState blockModel = new BlockModelRenderState();

    @Override
    public boolean hasSubState() {
        return !this.blockModel.isEmpty();
    }
}

