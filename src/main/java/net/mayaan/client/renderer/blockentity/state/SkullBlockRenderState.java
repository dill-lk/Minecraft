/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.blockentity.state;

import com.maayanlabs.math.Transformation;
import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.world.level.block.SkullBlock;

public class SkullBlockRenderState
extends BlockEntityRenderState {
    public float animationProgress;
    public Transformation transformation = Transformation.IDENTITY;
    public SkullBlock.Type skullType = SkullBlock.Types.ZOMBIE;
    public RenderType renderType;
}

