/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.slime.SlimeModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.LivingEntityRenderer;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.SlimeRenderer;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.SlimeRenderState;
import net.mayaan.client.renderer.rendertype.RenderTypes;

public class SlimeOuterLayer
extends RenderLayer<SlimeRenderState, SlimeModel> {
    private final SlimeModel model;

    public SlimeOuterLayer(RenderLayerParent<SlimeRenderState, SlimeModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new SlimeModel(modelSet.bakeLayer(ModelLayers.SLIME_OUTER));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SlimeRenderState state, float yRot, float xRot) {
        boolean appearsGlowingWithInvisibility;
        boolean bl = appearsGlowingWithInvisibility = state.appearsGlowing() && state.isInvisible;
        if (state.isInvisible && !appearsGlowingWithInvisibility) {
            return;
        }
        int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0f);
        if (appearsGlowingWithInvisibility) {
            submitNodeCollector.order(1).submitModel(this.model, state, poseStack, RenderTypes.outline(SlimeRenderer.SLIME_LOCATION), lightCoords, overlayCoords, -1, null, state.outlineColor, null);
        } else {
            submitNodeCollector.order(1).submitModel(this.model, state, poseStack, RenderTypes.entityTranslucent(SlimeRenderer.SLIME_LOCATION), lightCoords, overlayCoords, -1, null, state.outlineColor, null);
        }
    }
}

