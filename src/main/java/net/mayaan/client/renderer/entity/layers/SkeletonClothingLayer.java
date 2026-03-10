/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.EntityModel;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.monster.skeleton.SkeletonModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.SkeletonRenderState;
import net.mayaan.resources.Identifier;

public class SkeletonClothingLayer<S extends SkeletonRenderState, M extends EntityModel<S>>
extends RenderLayer<S, M> {
    private final SkeletonModel<S> layerModel;
    private final Identifier clothesLocation;

    public SkeletonClothingLayer(RenderLayerParent<S, M> renderer, EntityModelSet models, ModelLayerLocation layerLocation, Identifier clothesLocation) {
        super(renderer);
        this.clothesLocation = clothesLocation;
        this.layerModel = new SkeletonModel(models.bakeLayer(layerLocation));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        SkeletonClothingLayer.coloredCutoutModelCopyLayerRender(this.layerModel, this.clothesLocation, poseStack, submitNodeCollector, lightCoords, state, -1, 1);
    }
}

