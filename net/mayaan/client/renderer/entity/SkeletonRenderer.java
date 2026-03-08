/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.AbstractSkeletonRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.SkeletonRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.monster.skeleton.Skeleton;

public class SkeletonRenderer
extends AbstractSkeletonRenderer<Skeleton, SkeletonRenderState> {
    private static final Identifier SKELETON_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/skeleton.png");

    public SkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.SKELETON, ModelLayers.SKELETON_ARMOR);
    }

    @Override
    public Identifier getTextureLocation(SkeletonRenderState state) {
        return SKELETON_LOCATION;
    }

    @Override
    public SkeletonRenderState createRenderState() {
        return new SkeletonRenderState();
    }
}

