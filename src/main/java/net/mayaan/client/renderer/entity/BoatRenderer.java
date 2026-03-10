/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.EntityModel;
import net.mayaan.client.model.Model;
import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.object.boat.BoatModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.AbstractBoatRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.BoatRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Unit;

public class BoatRenderer
extends AbstractBoatRenderer {
    private final Model.Simple waterPatchModel;
    private final Identifier texture;
    private final EntityModel<BoatRenderState> model;

    public BoatRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelId) {
        super(context);
        this.texture = modelId.model().withPath(p -> "textures/entity/" + p + ".png");
        this.waterPatchModel = new Model.Simple(context.bakeLayer(ModelLayers.BOAT_WATER_PATCH), t -> RenderTypes.waterMask());
        this.model = new BoatModel(context.bakeLayer(modelId));
    }

    @Override
    protected EntityModel<BoatRenderState> model() {
        return this.model;
    }

    @Override
    protected RenderType renderType() {
        return this.model.renderType(this.texture);
    }

    @Override
    protected void submitTypeAdditions(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
        if (!state.isUnderWater) {
            submitNodeCollector.submitModel(this.waterPatchModel, Unit.INSTANCE, poseStack, this.waterPatchModel.renderType(this.texture), lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        }
    }
}

