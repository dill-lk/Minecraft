/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import net.mayaan.client.model.animal.bee.BeeStingerModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.player.PlayerModel;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.LivingEntityRenderer;
import net.mayaan.client.renderer.entity.layers.StuckInBodyLayer;
import net.mayaan.client.renderer.entity.state.AvatarRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Unit;

public class BeeStingerLayer<M extends PlayerModel>
extends StuckInBodyLayer<M, Unit> {
    private static final Identifier BEE_STINGER_LOCATION = Identifier.withDefaultNamespace("textures/entity/bee/bee_stinger.png");

    public BeeStingerLayer(LivingEntityRenderer<?, AvatarRenderState, M> renderer, EntityRendererProvider.Context context) {
        super(renderer, new BeeStingerModel(context.bakeLayer(ModelLayers.BEE_STINGER)), Unit.INSTANCE, BEE_STINGER_LOCATION, StuckInBodyLayer.PlacementStyle.ON_SURFACE);
    }

    @Override
    protected int numStuck(AvatarRenderState state) {
        return state.stingerCount;
    }
}

