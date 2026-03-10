/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.block.model.properties.conditional;

import net.mayaan.client.renderer.block.model.properties.conditional.ConditionalBlockModelProperty;
import net.mayaan.client.renderer.blockentity.ChestRenderer;
import net.mayaan.world.level.block.state.BlockState;

public class IsXmas
implements ConditionalBlockModelProperty {
    @Override
    public boolean get(BlockState blockState) {
        return ChestRenderer.xmasTextures();
    }
}

