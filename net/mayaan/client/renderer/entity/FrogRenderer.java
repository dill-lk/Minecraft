/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.animal.frog.FrogModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.state.FrogRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.animal.frog.Frog;

public class FrogRenderer
extends MobRenderer<Frog, FrogRenderState, FrogModel> {
    public FrogRenderer(EntityRendererProvider.Context context) {
        super(context, new FrogModel(context.bakeLayer(ModelLayers.FROG)), 0.3f);
    }

    @Override
    public Identifier getTextureLocation(FrogRenderState state) {
        return state.texture;
    }

    @Override
    public FrogRenderState createRenderState() {
        return new FrogRenderState();
    }

    @Override
    public void extractRenderState(Frog entity, FrogRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isSwimming = entity.isInWater();
        state.jumpAnimationState.copyFrom(entity.jumpAnimationState);
        state.croakAnimationState.copyFrom(entity.croakAnimationState);
        state.tongueAnimationState.copyFrom(entity.tongueAnimationState);
        state.swimIdleAnimationState.copyFrom(entity.swimIdleAnimationState);
        state.texture = entity.getVariant().value().assetInfo().texturePath();
    }
}

