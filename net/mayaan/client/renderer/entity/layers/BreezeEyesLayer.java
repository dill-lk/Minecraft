/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.breeze.BreezeModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.BreezeRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.resources.Identifier;

public class BreezeEyesLayer
extends RenderLayer<BreezeRenderState, BreezeModel> {
    private static final RenderType BREEZE_EYES = RenderTypes.breezeEyes(Identifier.withDefaultNamespace("textures/entity/breeze/breeze_eyes.png"));
    private final BreezeModel model;

    public BreezeEyesLayer(RenderLayerParent<BreezeRenderState, BreezeModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new BreezeModel(modelSet.bakeLayer(ModelLayers.BREEZE_EYES));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, BreezeRenderState state, float yRot, float xRot) {
        submitNodeCollector.order(1).submitModel(this.model, state, poseStack, BREEZE_EYES, lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
    }
}

