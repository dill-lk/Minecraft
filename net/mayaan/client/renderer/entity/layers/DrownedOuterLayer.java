/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.zombie.BabyDrownedModel;
import net.mayaan.client.model.monster.zombie.DrownedModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.ZombieRenderState;
import net.mayaan.resources.Identifier;

public class DrownedOuterLayer
extends RenderLayer<ZombieRenderState, DrownedModel> {
    private static final Identifier DROWNED_OUTER_LAYER_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/drowned_outer_layer.png");
    private static final Identifier BABY_DROWNED_OUTER_LAYER_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/drowned_outer_layer_baby.png");
    private final DrownedModel model;
    private final DrownedModel babyModel;

    public DrownedOuterLayer(RenderLayerParent<ZombieRenderState, DrownedModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new DrownedModel(modelSet.bakeLayer(ModelLayers.DROWNED_OUTER_LAYER));
        this.babyModel = new BabyDrownedModel(modelSet.bakeLayer(ModelLayers.DROWNED_BABY_OUTER_LAYER));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, ZombieRenderState state, float yRot, float xRot) {
        DrownedModel model = state.isBaby ? this.babyModel : this.model;
        Identifier layerLocation = state.isBaby ? BABY_DROWNED_OUTER_LAYER_LOCATION : DROWNED_OUTER_LAYER_LOCATION;
        DrownedOuterLayer.coloredCutoutModelCopyLayerRender(model, layerLocation, poseStack, submitNodeCollector, lightCoords, state, -1, 1);
    }
}

