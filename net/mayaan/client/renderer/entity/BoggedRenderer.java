/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.skeleton.BoggedModel;
import net.mayaan.client.model.monster.skeleton.SkeletonModel;
import net.mayaan.client.renderer.entity.AbstractSkeletonRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.layers.SkeletonClothingLayer;
import net.mayaan.client.renderer.entity.state.BoggedRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.monster.skeleton.Bogged;

public class BoggedRenderer
extends AbstractSkeletonRenderer<Bogged, BoggedRenderState> {
    private static final Identifier BOGGED_SKELETON_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/bogged.png");
    private static final Identifier BOGGED_OUTER_LAYER_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/bogged_overlay.png");

    public BoggedRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.BOGGED_ARMOR, new BoggedModel(context.bakeLayer(ModelLayers.BOGGED)));
        this.addLayer(new SkeletonClothingLayer<BoggedRenderState, SkeletonModel<BoggedRenderState>>(this, context.getModelSet(), ModelLayers.BOGGED_OUTER_LAYER, BOGGED_OUTER_LAYER_LOCATION));
    }

    @Override
    public Identifier getTextureLocation(BoggedRenderState state) {
        return BOGGED_SKELETON_LOCATION;
    }

    @Override
    public BoggedRenderState createRenderState() {
        return new BoggedRenderState();
    }

    @Override
    public void extractRenderState(Bogged entity, BoggedRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isSheared = entity.isSheared();
    }
}

