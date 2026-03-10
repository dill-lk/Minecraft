/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.animal.sheep.BabySheepModel;
import net.mayaan.client.model.animal.sheep.SheepModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.AgeableMobRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.layers.SheepWoolLayer;
import net.mayaan.client.renderer.entity.layers.SheepWoolUndercoatLayer;
import net.mayaan.client.renderer.entity.state.SheepRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.animal.sheep.Sheep;

public class SheepRenderer
extends AgeableMobRenderer<Sheep, SheepRenderState, SheepModel> {
    private static final Identifier SHEEP_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep.png");
    private static final Identifier SHEEP_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep_baby.png");

    public SheepRenderer(EntityRendererProvider.Context context) {
        super(context, new SheepModel(context.bakeLayer(ModelLayers.SHEEP)), new BabySheepModel(context.bakeLayer(ModelLayers.SHEEP_BABY)), 0.7f);
        this.addLayer(new SheepWoolUndercoatLayer(this, context.getModelSet()));
        this.addLayer(new SheepWoolLayer(this, context.getModelSet()));
    }

    @Override
    public Identifier getTextureLocation(SheepRenderState state) {
        return state.isBaby ? SHEEP_BABY_LOCATION : SHEEP_LOCATION;
    }

    @Override
    public SheepRenderState createRenderState() {
        return new SheepRenderState();
    }

    @Override
    public void extractRenderState(Sheep entity, SheepRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.headEatAngleScale = entity.getHeadEatAngleScale(partialTicks);
        state.headEatPositionScale = entity.getHeadEatPositionScale(partialTicks);
        state.isSheared = entity.isSheared();
        state.woolColor = entity.getColor();
        state.isJebSheep = SheepRenderer.checkMagicName(entity, "jeb_");
    }
}

