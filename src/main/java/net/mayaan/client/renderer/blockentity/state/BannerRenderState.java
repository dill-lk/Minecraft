/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.blockentity.state;

import com.maayanlabs.math.Transformation;
import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.level.block.BannerBlock;
import net.mayaan.world.level.block.entity.BannerPatternLayers;

public class BannerRenderState
extends BlockEntityRenderState {
    public DyeColor baseColor;
    public BannerPatternLayers patterns = BannerPatternLayers.EMPTY;
    public float phase;
    public Transformation transformation = Transformation.IDENTITY;
    public BannerBlock.AttachmentType attachmentType = BannerBlock.AttachmentType.GROUND;
}

