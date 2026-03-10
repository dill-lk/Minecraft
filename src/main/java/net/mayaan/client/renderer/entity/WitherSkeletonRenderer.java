/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.AbstractSkeletonRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.SkeletonRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.monster.skeleton.WitherSkeleton;

public class WitherSkeletonRenderer
extends AbstractSkeletonRenderer<WitherSkeleton, SkeletonRenderState> {
    private static final Identifier WITHER_SKELETON_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/wither_skeleton.png");

    public WitherSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.WITHER_SKELETON, ModelLayers.WITHER_SKELETON_ARMOR);
    }

    @Override
    public Identifier getTextureLocation(SkeletonRenderState state) {
        return WITHER_SKELETON_LOCATION;
    }

    @Override
    public SkeletonRenderState createRenderState() {
        return new SkeletonRenderState();
    }
}

