/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.animal.ghast.HappyGhastModel;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.HappyGhastRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.resources.Identifier;
import net.mayaan.tags.ItemTags;

public class RopesLayer<M extends HappyGhastModel>
extends RenderLayer<HappyGhastRenderState, M> {
    private final RenderType ropes;
    private final HappyGhastModel adultModel;
    private final HappyGhastModel babyModel;

    public RopesLayer(RenderLayerParent<HappyGhastRenderState, M> renderer, EntityModelSet modelSet, Identifier ropesTexture) {
        super(renderer);
        this.ropes = RenderTypes.entityCutout(ropesTexture);
        this.adultModel = new HappyGhastModel(modelSet.bakeLayer(ModelLayers.HAPPY_GHAST_ROPES));
        this.babyModel = new HappyGhastModel(modelSet.bakeLayer(ModelLayers.HAPPY_GHAST_BABY_ROPES));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, HappyGhastRenderState state, float yRot, float xRot) {
        if (!state.isLeashHolder || !state.bodyItem.is(ItemTags.HARNESSES)) {
            return;
        }
        HappyGhastModel model = state.isBaby ? this.babyModel : this.adultModel;
        submitNodeCollector.submitModel(model, state, poseStack, this.ropes, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
    }
}

