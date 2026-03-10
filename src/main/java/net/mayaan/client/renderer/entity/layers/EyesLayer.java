/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.EntityModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.client.renderer.feature.ModelFeatureRenderer;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.client.renderer.texture.TextureAtlasSprite;

public abstract class EyesLayer<S extends EntityRenderState, M extends EntityModel<S>>
extends RenderLayer<S, M> {
    public EyesLayer(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        submitNodeCollector.order(1).submitModel(this.getParentModel(), state, poseStack, this.renderType(), lightCoords, OverlayTexture.NO_OVERLAY, -1, (TextureAtlasSprite)null, ((EntityRenderState)state).outlineColor, (ModelFeatureRenderer.CrumblingOverlay)null);
    }

    public abstract RenderType renderType();
}

