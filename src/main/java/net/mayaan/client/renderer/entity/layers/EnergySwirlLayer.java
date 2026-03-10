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
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.client.renderer.texture.TextureAtlasSprite;
import net.mayaan.resources.Identifier;

public abstract class EnergySwirlLayer<S extends EntityRenderState, M extends EntityModel<S>>
extends RenderLayer<S, M> {
    public EnergySwirlLayer(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        if (!this.isPowered(state)) {
            return;
        }
        float t = ((EntityRenderState)state).ageInTicks;
        M model = this.model();
        submitNodeCollector.order(1).submitModel(model, state, poseStack, RenderTypes.energySwirl(this.getTextureLocation(), this.xOffset(t) % 1.0f, t * 0.01f % 1.0f), lightCoords, OverlayTexture.NO_OVERLAY, -8355712, (TextureAtlasSprite)null, ((EntityRenderState)state).outlineColor, (ModelFeatureRenderer.CrumblingOverlay)null);
    }

    protected abstract boolean isPowered(S var1);

    protected abstract float xOffset(float var1);

    protected abstract Identifier getTextureLocation();

    protected abstract M model();
}

