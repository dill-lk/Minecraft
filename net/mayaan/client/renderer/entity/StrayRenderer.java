/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.skeleton.SkeletonModel;
import net.mayaan.client.renderer.entity.AbstractSkeletonRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.layers.SkeletonClothingLayer;
import net.mayaan.client.renderer.entity.state.SkeletonRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.monster.skeleton.Stray;

public class StrayRenderer
extends AbstractSkeletonRenderer<Stray, SkeletonRenderState> {
    private static final Identifier STRAY_SKELETON_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/stray.png");
    private static final Identifier STRAY_CLOTHES_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/stray_overlay.png");

    public StrayRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.STRAY, ModelLayers.STRAY_ARMOR);
        this.addLayer(new SkeletonClothingLayer<SkeletonRenderState, SkeletonModel<SkeletonRenderState>>(this, context.getModelSet(), ModelLayers.STRAY_OUTER_LAYER, STRAY_CLOTHES_LOCATION));
    }

    @Override
    public Identifier getTextureLocation(SkeletonRenderState state) {
        return STRAY_SKELETON_LOCATION;
    }

    @Override
    public SkeletonRenderState createRenderState() {
        return new SkeletonRenderState();
    }
}

