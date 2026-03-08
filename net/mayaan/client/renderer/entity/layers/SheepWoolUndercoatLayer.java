/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.EntityModel;
import net.mayaan.client.model.animal.sheep.SheepFurModel;
import net.mayaan.client.model.animal.sheep.SheepModel;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.SheepRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.item.DyeColor;

public class SheepWoolUndercoatLayer
extends RenderLayer<SheepRenderState, SheepModel> {
    private static final Identifier SHEEP_WOOL_UNDERCOAT_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep_wool_undercoat.png");
    private final EntityModel<SheepRenderState> model;

    public SheepWoolUndercoatLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_WOOL_UNDERCOAT));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SheepRenderState state, float yRot, float xRot) {
        if (state.isInvisible || !state.isJebSheep && state.woolColor == DyeColor.WHITE || state.isBaby) {
            return;
        }
        SheepWoolUndercoatLayer.coloredCutoutModelCopyLayerRender(this.model, SHEEP_WOOL_UNDERCOAT_LOCATION, poseStack, submitNodeCollector, lightCoords, state, state.getWoolColor(), 1);
    }
}

