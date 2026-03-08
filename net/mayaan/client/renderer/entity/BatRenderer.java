/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.ambient.BatModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.state.BatRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.ambient.Bat;

public class BatRenderer
extends MobRenderer<Bat, BatRenderState, BatModel> {
    private static final Identifier BAT_LOCATION = Identifier.withDefaultNamespace("textures/entity/bat/bat.png");

    public BatRenderer(EntityRendererProvider.Context context) {
        super(context, new BatModel(context.bakeLayer(ModelLayers.BAT)), 0.25f);
    }

    @Override
    public Identifier getTextureLocation(BatRenderState state) {
        return BAT_LOCATION;
    }

    @Override
    public BatRenderState createRenderState() {
        return new BatRenderState();
    }

    @Override
    public void extractRenderState(Bat entity, BatRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isResting = entity.isResting();
        state.flyAnimationState.copyFrom(entity.flyAnimationState);
        state.restAnimationState.copyFrom(entity.restAnimationState);
    }
}

