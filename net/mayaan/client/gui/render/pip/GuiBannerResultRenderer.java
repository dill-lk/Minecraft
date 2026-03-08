/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.render.pip;

import com.maayanlabs.blaze3d.platform.Lighting;
import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.render.pip.PictureInPictureRenderer;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.Sheets;
import net.mayaan.client.renderer.SubmitNodeStorage;
import net.mayaan.client.renderer.blockentity.BannerRenderer;
import net.mayaan.client.renderer.feature.FeatureRenderDispatcher;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.state.gui.pip.GuiBannerResultRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.client.resources.model.sprite.SpriteGetter;

public class GuiBannerResultRenderer
extends PictureInPictureRenderer<GuiBannerResultRenderState> {
    private final SpriteGetter sprites;

    public GuiBannerResultRenderer(MultiBufferSource.BufferSource bufferSource, SpriteGetter sprites) {
        super(bufferSource);
        this.sprites = sprites;
    }

    @Override
    public Class<GuiBannerResultRenderState> getRenderStateClass() {
        return GuiBannerResultRenderState.class;
    }

    @Override
    protected void renderToTexture(GuiBannerResultRenderState renderState, PoseStack poseStack) {
        Mayaan.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
        poseStack.translate(0.0f, 0.25f, 0.0f);
        FeatureRenderDispatcher featureRenderDispatcher = Mayaan.getInstance().gameRenderer.getFeatureRenderDispatcher();
        SubmitNodeStorage submitNodeStorage = featureRenderDispatcher.getSubmitNodeStorage();
        submitNodeStorage.submitModel(renderState.flag(), Float.valueOf(0.0f), poseStack, Sheets.BANNER_BASE.renderType(RenderTypes::entitySolid), 0xF000F0, OverlayTexture.NO_OVERLAY, -1, this.sprites.get(Sheets.BANNER_BASE), 0, null);
        BannerRenderer.submitPatterns(this.sprites, poseStack, submitNodeStorage, 0xF000F0, OverlayTexture.NO_OVERLAY, renderState.flag(), Float.valueOf(0.0f), true, renderState.baseColor(), renderState.resultBannerPatterns(), null);
        featureRenderDispatcher.renderAllFeatures();
    }

    @Override
    protected String getTextureLabel() {
        return "banner result";
    }
}

