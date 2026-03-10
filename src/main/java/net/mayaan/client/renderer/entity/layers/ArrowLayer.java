/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.object.projectile.ArrowModel;
import net.mayaan.client.model.player.PlayerModel;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.LivingEntityRenderer;
import net.mayaan.client.renderer.entity.TippableArrowRenderer;
import net.mayaan.client.renderer.entity.layers.StuckInBodyLayer;
import net.mayaan.client.renderer.entity.state.ArrowRenderState;
import net.mayaan.client.renderer.entity.state.AvatarRenderState;

public class ArrowLayer<M extends PlayerModel>
extends StuckInBodyLayer<M, ArrowRenderState> {
    public ArrowLayer(LivingEntityRenderer<?, AvatarRenderState, M> renderer, EntityRendererProvider.Context context) {
        super(renderer, new ArrowModel(context.bakeLayer(ModelLayers.ARROW)), new ArrowRenderState(), TippableArrowRenderer.NORMAL_ARROW_LOCATION, StuckInBodyLayer.PlacementStyle.IN_CUBE);
    }

    @Override
    protected int numStuck(AvatarRenderState state) {
        return state.arrowCount;
    }
}

